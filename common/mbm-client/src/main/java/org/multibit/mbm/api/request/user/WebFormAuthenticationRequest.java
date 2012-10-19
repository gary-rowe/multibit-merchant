package org.multibit.mbm.api.request.user;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>Base request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state common to all interactions with the User entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class WebFormAuthenticationRequest {

  /**
   * A username (optional for anonymity reasons)
   */
  @JsonProperty
  private String username = null;

  /**
   * A user password (optional for anonymity reasons)
   */
  @JsonProperty
  private String passwordDigest = null;

  /**
   * @return The password digest
   */
  public String getPasswordDigest() {
    return passwordDigest;
  }

  public void setPasswordDigest(String passwordDigest) {
    this.passwordDigest = passwordDigest;
  }

  /**
   * @return The user name
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
