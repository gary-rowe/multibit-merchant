package org.multibit.mbm.auth.hmac;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 * TODO Requires implementation
 * http://rc3.org/2011/12/02/using-hmac-to-authenticate-web-service-requests/ 
 */

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class HmacAuthInjectable<T> extends AbstractHttpContextInjectable<T> {
  private static final String PREFIX = "HMAC";
  private static final String HEADER_VALUE = PREFIX + " realm=\"%s\"";

  private final Authenticator<HmacCredentials, T> authenticator;
  private final String realm;
  private final boolean required;

  HmacAuthInjectable(Authenticator<HmacCredentials, T> authenticator, String realm, boolean required) {
    this.authenticator = authenticator;
    this.realm = realm;
    this.required = required;
  }

  public Authenticator<HmacCredentials, T> getAuthenticator() {
    return authenticator;
  }

  public String getRealm() {
    return realm;
  }

  public boolean isRequired() {
    return required;
  }

  @Override
  public T getValue(HttpContext c) {

    try {
      final String header = c.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
      if (header != null) {

        final String[] authTokens = header.split(" ");

        if (authTokens.length != 3) {
          // Malformed
          HmacAuthProvider.LOG.debug("Error decoding credentials (length is {})", authTokens.length);
          throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

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

        final HmacCredentials credentials = new HmacCredentials(apiKey, signature, contents);

        final Optional<T> result = authenticator.authenticate(credentials);
        if (result.isPresent()) {
          return result.get();
        }
      }
    } catch (IllegalArgumentException e) {
      HmacAuthProvider.LOG.debug(e, "Error decoding credentials");
    } catch (AuthenticationException e) {
      HmacAuthProvider.LOG.warn(e, "Error authenticating credentials");
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    if (required) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
        .header(HttpHeaders.AUTHORIZATION,
          String.format(HEADER_VALUE, realm))
        .entity("Credentials are required to access this resource.")
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build());
    }
    return null;
  }
}

