package org.multibit.mbm.auth.openid;

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
 *         
 */
import com.google.common.base.Charsets;
import com.google.common.base.Objects;

import java.security.MessageDigest;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A set of user-provided Basic Authentication credentials, consisting of a username and a password.
 */
public class OpenIdCredentials {
  private final String username;
  private final String password;

  /**
   * Creates a new {@link OpenIdCredentials} with the given username and password.
   *
   * @param username    the username
   * @param password    the password
   */
  public OpenIdCredentials(String username, String password) {
    this.username = checkNotNull(username);
    this.password = checkNotNull(password);
  }

  /**
   * Returns the credentials' username.
   *
   * @return the credentials' username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the credentials' password.
   *
   * @return the credentials' password
   */
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if ((obj == null) || (getClass() != obj.getClass())) { return false; }
    final OpenIdCredentials that = (OpenIdCredentials) obj;
    // N.B.: Do a constant-time comparison here to prevent timing attacks.
    final byte[] thisBytes = password.getBytes(Charsets.UTF_8);
    final byte[] thatBytes = that.password.getBytes(Charsets.UTF_8);
    return username.equals(that.username) && MessageDigest.isEqual(thisBytes, thatBytes);
  }

  @Override
  public int hashCode() {
    return (31 * username.hashCode()) + password.hashCode();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("username", username)
      .add("password", "**********")
      .toString();
  }
}
