package org.multibit.mbm.client.interfaces.rest.auth.webform;

import com.google.common.base.Objects;

/**
 * <p>Value object to provide the following to web form registration:</p>
 * <ul>
 * <li>Storage of the necessary registration details for later web form authentication</li>
 * </ul>
 * <p>A set of user-provided web form authentication credentials, consisting of a username and password.</p>
 * <p>The web form normally initiates the creation of an authentication cookie</p>
 *
 * @since 0.0.1
 */
public class WebFormClientRegistration extends WebFormClientCredentials {

  // TODO Add in the rest of the registration fields

  /**
   * @param username       The offered username (plaintext) that locates the user server-side
   * @param passwordDigest The offered password (one-pass digest) that seeds the multi-pass digest server-side
   */
  public WebFormClientRegistration(String username, String passwordDigest) {
    super(username, passwordDigest);
  }



  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("username", getUsername())
      .add("password", "***")
      .toString();
  }

}
