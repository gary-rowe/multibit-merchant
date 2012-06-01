package org.multibit.mbm.auth.hmac;

/**
 * <p>Injectable to provide the following to {@link HmacRestrictedToProvider}:</p>
 * <ul>
 * <li>Carries HMAC authentication data</li>
 * </ul>
 *
 * @since 0.0.1
 */

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.db.dto.Authority;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class HmacRestrictedToInjectable<T> extends AbstractHttpContextInjectable<T> {
  private static final String PREFIX = "HmacSHA1";
  private static final String HEADER_VALUE = PREFIX + " realm=\"%s\"";

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
  public T getValue(HttpContext c) {

    try {
      final String header = c.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
      if (header != null) {

        final String[] authTokens = header.split(" ");

        if (authTokens.length != 3) {
          // Malformed
          HmacRestrictedToProvider.LOG.debug("Error decoding credentials (length is {})", authTokens.length);
          throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        final String algorithm = authTokens[0];
        final String apiKey = authTokens[1];
        final String signature = authTokens[2];
        final String contents;

        // Determine which part of the request will be used for the content
        final String method = c.getRequest().getMethod().toUpperCase();
        if ("GET".equals(method) ||
          "HEAD".equals(method) ||
          "DELETE".equals(method)) {
          // No entity so use the URI
          contents = c.getRequest().getRequestUri().toString();
        } else {
          // Potentially have an entity (even in OPTIONS) so use that
          contents = c.getRequest().getEntity(String.class);
        }

        final HmacCredentials credentials = new HmacCredentials(algorithm, apiKey, signature, contents, authorities);

        final Optional<T> result = authenticator.authenticate(credentials);
        if (result.isPresent()) {
          return result.get();
        } else {

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
        String.format(HEADER_VALUE, realm))
      .entity("Credentials are required to access this resource.")
      .type(MediaType.TEXT_PLAIN_TYPE)
      .build());
  }
}

