package org.multibit.mbm.client.interfaces.rest.auth.hmac;


import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.container.ContainerRequest;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>Injectable to provide the following to {@link HmacServerRestrictedToProvider}:</p>
 * <ul>
 * <li>Performs decode from HTTP request</li>
 * <li>Carries HMAC authentication data</li>
 * </ul>
 *
 * @since 0.0.1
 */
class HmacServerRestrictedToInjectable<T> extends AbstractHttpContextInjectable<T> {

  private static final Logger log = LoggerFactory.getLogger(HmacServerRestrictedToInjectable.class);

  private final Authenticator<HmacServerCredentials, T> authenticator;
  private final String realm;
  private final Authority[] requiredAuthorities;

  /**
   * @param authenticator       The Authenticator that will compare credentials
   * @param realm               The authentication realm
   * @param requiredAuthorities The required authorities as provided by the RestrictedTo annotation
   */
  HmacServerRestrictedToInjectable(
    Authenticator<HmacServerCredentials, T> authenticator,
    String realm,
    Authority[] requiredAuthorities) {
    this.authenticator = authenticator;
    this.realm = realm;
    this.requiredAuthorities = requiredAuthorities;
  }

  public Authenticator<HmacServerCredentials, T> getAuthenticator() {
    return authenticator;
  }

  public String getRealm() {
    return realm;
  }

  public Authority[] getRequiredAuthorities() {
    return requiredAuthorities;
  }

  @Override
  public T getValue(HttpContext httpContext) {

    try {

      // Get the Authorization header
      final String header = httpContext.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
      if (header != null) {

        // Expect form of "Authorization: <Algorithm> <ApiKey> <Signature>"
        final String[] authTokens = header.split(" ");

        if (authTokens.length != 3) {
          // Malformed
          log.debug("Error decoding credentials (length is {})", authTokens.length);
          throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        final String apiKey = authTokens[1];
        final String signature = authTokens[2];
        final ContainerRequest containerRequest = (ContainerRequest) httpContext.getRequest();

        // Build the canonical representation for the server side
        final String canonicalRepresentation = HmacUtils.createCanonicalRepresentation(containerRequest);
        log.debug("Server side canonical representation: '{}'", canonicalRepresentation);

        final HmacServerCredentials credentials = new HmacServerCredentials("HmacSHA1", apiKey, signature, canonicalRepresentation, requiredAuthorities);

        final Optional<T> result = authenticator.authenticate(credentials);
        if (result.isPresent()) {
          return result.get();
        }
      }
    } catch (IllegalArgumentException e) {
      log.debug("Error decoding credentials", e);
    } catch (AuthenticationException e) {
      log.warn("Error authenticating credentials", e);
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

