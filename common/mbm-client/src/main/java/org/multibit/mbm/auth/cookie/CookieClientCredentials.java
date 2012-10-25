package org.multibit.mbm.auth.cookie;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.multibit.mbm.auth.Authority;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Value object to provide the following to cookie authenticator:</p>
 * <ul>
 * <li>Storage of the necessary credentials for cookie authentication</li>
 * </ul>
 * <p>A set of user-provided cookie authentication credentials, consisting of a session token and "remember me" token.
 * </p>
 *
 * @since 0.0.1
 */
public class CookieClientCredentials {

  /**
   * The sessionToken is a UUID that is only valid for the length of the session
   * and is obtained after a successful authentication
   * It may be absent during an authentication for the public role
   */
  private final Optional<UUID> sessionToken;

  /**
   * The rememberMeToken is a UUID that persists until cleared. It provides a partial login with reduced
   * access rights.
   */
  private final Optional<UUID> rememberMeToken;

  private final Authority[] requiredAuthorities;

  private final boolean isPublic;

  /**
   * @param sessionToken        The session token (expires when browser tab is closed)
   * @param rememberMeToken     The remember-me token (expires after a length of time)
   * @param requiredAuthorities The authorities required to authenticate (provided by the {@link org.multibit.mbm.auth.annotation.RestrictedTo} annotation)
   * @param isPublic            True if the authentication can be made purely at the client side
   */
  public CookieClientCredentials(
    Optional<UUID> sessionToken,
    Optional<UUID> rememberMeToken,
    Authority[] requiredAuthorities,
    boolean isPublic
  ) {
    this.sessionToken = checkNotNull(sessionToken);
    this.rememberMeToken = checkNotNull(rememberMeToken);
    this.requiredAuthorities = checkNotNull(requiredAuthorities);
    this.isPublic = isPublic;
  }

  /**
   * @return The temporary session token that authenticates this user
   */
  public Optional<UUID> getSessionToken() {
    return sessionToken;
  }

  /**
   * @return The long-lived remember me token that provides a partial login privilege
   */
  public Optional<UUID> getRememberMeToken() {
    return rememberMeToken;
  }

  /**
   * @return The authorities that are required to access to the resource
   */
  public Authority[] getRequiredAuthorities() {
    return requiredAuthorities;
  }

  /**
   * @return True if the authentication can be made purely at the client side (anonymous for session duration)
   */
  public boolean isPublic() {
    return isPublic;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("sessionToken", sessionToken)
      .add("rememberMeToken", rememberMeToken)
      .toString();
  }

}
