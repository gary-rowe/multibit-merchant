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
   * A user password (optional for anonymity reasons)
   */
  @JsonProperty
  private String passwordDigest = null;

  /**
   * An API key providing an anonymous method of identifying a user
   * Typically a UUID
   */
  @JsonProperty
  private String apiKey = null;

  /**
   * @return The API key to use to identify the user during HMAC authentication
   */
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

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
