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
public abstract class BaseUserRequest {

  /**
   * A username (optional for anonymity reasons)
   */
  @JsonProperty
  private String username = null;

  /**
   * A user password (not plaintext and optional for anonymity reasons)
   */
  @JsonProperty
  private String password = null;

  /**
   * A third-party provided UUID to act as an authentication token
   */
  @JsonProperty("open_id")
  private String openId = null;

  /**
   * @return An OpenId to use instead of a username/password combination
   */
  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  /**
   * @return The password (plaintext over HTTPS)
   */
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
