package org.multibit.mbm.auth.webform;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

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
  private final String passwordDigest;

  /**
   * @param username The offered username (plaintext) that locates the user server-side
   * @param passwordDigest The offered password (one-pass digest) that seeds the multi-pass digest server-side
   */

  public WebFormClientCredentials(
    String username,
    String passwordDigest
  ) {
    this.username = checkNotNull(username);
    this.passwordDigest = checkNotNull(passwordDigest);
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordDigest() {
    return passwordDigest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WebFormClientCredentials that = (WebFormClientCredentials) o;

    if (passwordDigest != null ? !passwordDigest.equals(that.passwordDigest) : that.passwordDigest != null)
      return false;
    if (username != null ? !username.equals(that.username) : that.username != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (passwordDigest != null ? passwordDigest.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("username", username)
      .add("password", "***")
      .toString();
  }

}
