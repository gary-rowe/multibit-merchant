package org.multibit.mbm.auth.hmac;


import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.container.ContainerRequest;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.db.dto.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>Injectable to provide the following to {@link org.multibit.mbm.auth.hmac.HmacRestrictedToProvider}:</p>
 * <ul>
 * <li>Performs decode from HTTP request</li>
 * <li>Carries HMAC authentication data</li>
 * </ul>
 *
 * <ol>
 * <li>Read the HTTP "Authorization" header</li>
 * <li>Read the HTTP "Content-Type" header (optional)</li>
 * <li>Read the HTTP "Content-MD5" header (optional)</li>
 * <li>Read the HTTP "X-HMAC-Nonce" header (optional) - </li>
 * <li>Read the HTTP "X-HMAC-Date" header (optional) - HTTP-Date format RFC1123 5.2.14 UTC</li>
 * </ol>
 * <p>Create the canonical representation of the request using the following algorithm:</p>
 * <ul>
 * <li>Start with the empty string ("").</li>
 * <li>Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).</li>
 * <li>Add the date for the request using the form "date:#date-of-request" followed by a single newline. The date for the signature must be formatted exactly as in the request.</li>
 * <li>Add the nonce for the request in the form "nonce:#nonce-in-request" followed by a single newline. If no nonce is passed use the empty string as nonce value.</li>
 * <li>Convert all remaining header names to lowercase.</li>
 * <li>Sort the remaining headers lexicographically by header name.</li>
 * <li>Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.</li>
 * <li>Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.</li>
 * <li>Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.</li>
 * <li>Append the path to the canonical representation</li>
 * <li>Append the url-decoded query parameters to the canonical representation</li>
 * <li>URL-decode query parameters if required</li>
 * </ul>
 * <p>There is no support for query-based authentication because this breaks the purpose of a URI as a resource
 * identifier, not as authenticator. It would lead to authenticated URIs being shared as permalinks which would
 * later fail through a TTL threshold being exceeded.</p>
 * <p>Examples</p>
 * <h3>Example with X-HMAC-Nonce</h3>
 * <pre>
 * GET /example/resource.html?sort=header%20footer&order=ASC HTTP/1.1
 * Host: www.example.org
 * Date: Mon, 20 Jun 2011 12:06:11 GMT
 * User-Agent: curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3
 * X-MAC-Nonce: Thohn2Mohd2zugoo
 * </pre>
 * <p>Applying the above gives a canonical representation of</p>
 * <pre>
 * GET\n
 * date:Mon, 20 Jun 2011 12:06:11 GMT\n
 * nonce:Thohn2Mohd2zugo\n
 * /example/resource.html?order=ASC&sort=header footer
 * </pre>
 * <p>This would be passed into the HMAC signature </p>
 * <h3>Example with X-HMAC-Date</h3>
 * <p>Given the following request:</p>
 * <pre>
 * GET /example/resource.html?sort=header%20footer&order=ASC HTTP/1.1
 * Host: www.example.org
 * Date: Mon, 20 Jun 2011 12:06:11 GMT
 * User-Agent: curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3
 * X-MAC-Nonce: Thohn2Mohd2zugoo
 * X-MAC-Date: Mon, 20 Jun 2011 14:06:57 GMT
 * </pre>
 * <p>The canonical representation is:</p>
 * <pre> GET\n
 * date:Mon, 20 Jun 2011 14:06:57 GMT\n
 * nonce:Thohn2Mohd2zugo\n
 * /example/resource.html?order=ASC&sort=header footer
 * </pre>
 * <p>Based on <a href=""http://rubydoc.info/gems/warden-hmac-authentication/0.6.1/file/README.md></a>the Warden HMAC Ruby gem</a>.</p>
 *
 * @since 0.0.1
 */
class HmacRestrictedToInjectable<T> extends AbstractHttpContextInjectable<T> {

  private final Logger log = LoggerFactory.getLogger(HmacRestrictedToInjectable.class);

  private final Authenticator<HmacCredentials, T> authenticator;
  private final String realm;
  private final Authority[] authorities;

  HmacRestrictedToInjectable(Authenticator<HmacCredentials, T> authenticator, String realm, Authority[] authorities) {
    this.authenticator = authenticator;
    this.realm = realm;
    this.authorities = authorities;
  }

  public Authenticator<HmacCredentials, T> getAuthenticator() {
    return authenticator;
  }

  public String getRealm() {
    return realm;
  }

  public Authority[] getAuthorities() {
    return authorities;
  }

  @Override
  public T getValue(HttpContext httpContext) {

    try {
      final String header = httpContext.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
      if (header != null) {

        final String[] authTokens = header.split(" ");

        if (authTokens.length != 3) {
          // Malformed
          HmacRestrictedToProvider.LOG.debug("Error decoding credentials (length is {})", authTokens.length);
          throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        final String apiKey = authTokens[1];
        final String signature = authTokens[2];
        final ContainerRequest containerRequest = (ContainerRequest) httpContext.getRequest();

        // Build the canonical representation for the server side
        final String canonicalRepresentation = HmacUtils.createCanonicalRepresentation(containerRequest);
        log.debug("Server side canonical representation: '{}'",canonicalRepresentation);

        final HmacCredentials credentials = new HmacCredentials("HmacSHA1", apiKey, signature, canonicalRepresentation, authorities);

        final Optional<T> result = authenticator.authenticate(credentials);
        if (result.isPresent()) {
          return result.get();
        }
      }
    } catch (IllegalArgumentException e) {
      HmacRestrictedToProvider.LOG.debug(e, "Error decoding credentials");
    } catch (AuthenticationException e) {
      HmacRestrictedToProvider.LOG.warn(e, "Error authenticating credentials");
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    // Must have failed to be here
    throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
      .header(HttpHeaders.AUTHORIZATION,
        String.format(HmacUtils.HEADER_VALUE, realm))
      .entity("Credentials are required to access this resource.")
      .type(MediaType.TEXT_PLAIN_TYPE)
      .build());
  }

}

