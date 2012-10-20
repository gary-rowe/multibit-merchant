package org.multibit.mbm.auth.cookie;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.auth.Authority;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Injectable to provide the following to {@link CookieClientRestrictedToProvider}:</p>
 * <ul>
 * <li>Performs decode from HTTP request cookie</li>
 * <li>Carries cookie authentication data</li>
 * </ul>
 *
 * @since 0.0.1
 */
class CookieClientRestrictedToInjectable<T> extends AbstractHttpContextInjectable<T> {

  private final Authenticator<CookieClientCredentials, T> authenticator;
  private final String sessionTokenName;
  private final String rememberMeName;
  private final Authority[] requiredAuthorities;

  /**
   * @param authenticator The Authenticator that will compare credentials
   * @param sessionTokenName The session token cookie name (short lived with full access)
   * @param rememberMeName The "remember me" cookie name (long lived but reduced privilege)
   * @param requiredAuthorities The required authorities as provided by the RestrictedTo annotation
   */
  CookieClientRestrictedToInjectable(
    Authenticator<CookieClientCredentials, T> authenticator,
    String sessionTokenName,
    String rememberMeName,
    Authority[] requiredAuthorities) {
    this.authenticator = authenticator;
    this.sessionTokenName = sessionTokenName;
    this.rememberMeName = rememberMeName;
    this.requiredAuthorities = requiredAuthorities;
  }

  public Authenticator<CookieClientCredentials, T> getAuthenticator() {
    return authenticator;
  }

  public String getSessionTokenName() {
    return sessionTokenName;
  }

  public String getRememberMeName() {
    return rememberMeName;
  }

  public Authority[] getRequiredAuthorities() {
    return requiredAuthorities;
  }

  @Override
  public T getValue(HttpContext httpContext) {

    Map<String, Cookie> cookies = httpContext.getRequest().getCookies();

    try {

      Optional<UUID> sessionToken = Optional.absent();
      Optional<UUID> rememberMeToken = Optional.absent();

      // Configure the UUIDs based on cookie values (must be valid UUIDs)
      Cookie sessionTokenCookie = cookies.get(sessionToken);
      Cookie rememberMeTokenCookie = cookies.get(rememberMeToken);
      if (sessionTokenCookie != null) {
        sessionToken = Optional.of(UUID.fromString(sessionTokenCookie.getValue()));
      }
      if (rememberMeTokenCookie != null) {
        rememberMeToken = Optional.of(UUID.fromString(rememberMeTokenCookie.getValue()));
      }

      // Build the credentials
      final CookieClientCredentials credentials = new CookieClientCredentials(sessionToken.get(), rememberMeToken, requiredAuthorities);

      // Authenticate
      final Optional<T> result = authenticator.authenticate(credentials);
      if (result.isPresent()) {
        return result.get();
      }

    } catch (IllegalArgumentException e) {
      CookieClientRestrictedToProvider.LOG.warn(e, "Error decoding credentials (not a UUID)");
    } catch (IllegalStateException e) {
      CookieClientRestrictedToProvider.LOG.warn(e, "Error decoding credentials (no session token)");
    } catch (AuthenticationException e) {
      CookieClientRestrictedToProvider.LOG.warn(e, "Error authenticating credentials");
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    // Must have failed to be here
    // TODO Add login page content (some kind of view injection?)
    throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
      .entity("Credentials are required to access this resource")
      .type(MediaType.TEXT_PLAIN_TYPE)
      .build());
  }

}

