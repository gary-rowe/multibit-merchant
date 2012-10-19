package org.multibit.mbm.api.request.user;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to create an initial bare bones User by an administrator</li>
 * </ul>
 * <p>Note that subsequent updates to the User can set more detail into the User as required</p>
 * <p>When an administrator creates a User there is a lot more detail that can be added that is
 * not available to the general public.</p>
 *
 * @since 0.0.1
 *         
 */
public class AdminCreateUserRequest extends WebFormAuthenticationRequest {

  /**
   * An API key providing an anonymous method of identifying a user
   * Typically a UUID
   */
  @JsonProperty
  private String apiKey = null;

  /**
   * <p>Used as a shared secret between this user and the application. Typically
   * part of an HMAC authentication scheme.</p>
   */
  @JsonProperty("secret_key")
  private String secretKey = null;

  /**
   * @return The API key to use to identify the user during HMAC authentication
   */
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }


}
