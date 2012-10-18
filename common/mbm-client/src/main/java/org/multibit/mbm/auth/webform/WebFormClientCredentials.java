package org.multibit.mbm.auth.webform;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * <p>Value object to provide the following to web form authenticator:</p>
 * <ul>
 * <li>Storage of the necessary credentials for web form authentication</li>
 * </ul>
 * <p>A set of user-provided web form authentication credentials, consisting of a username and password.</p>
 * <p>The web form normally initiates the creation of an authentication cookie</p>
 *
 * @since 0.0.1
 */
public class WebFormClientCredentials {

  private final String username;
  private final String digestedPassword;

  /**
   * @param username The offered username (plaintext)
   * @param digestedPassword The offered password (digested)
   */

  public WebFormClientCredentials(
    String username,
    String digestedPassword
  ) {
    this.username = checkNotNull(username);
    this.digestedPassword = checkNotNull(digestedPassword);
    checkState(digestedPassword.length()>12,"String is too short to be a good digest");
  }

  public String getUsername() {
    return username;
  }

  public String getDigestedPassword() {
    return digestedPassword;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("username", username)
      .add("password", "***")
      .toString();
  }

}
