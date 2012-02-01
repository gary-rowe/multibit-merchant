package org.multibit.mbm.web.rest.v1.client.security;

import org.multibit.mbm.web.rest.v1.client.BaseRequest;

/**
 *  <p>Request to provide the following to controllers:</p>
 *  <ul>
 *  <li>Provision of client state to create an initial bare bones User</li>
 *  </ul>
 * <p>Note that subsequent updates to the User can set more detail into the User as required</p>
 *
 * @since 1.0.0
 *         
 */
public class CreateUserRequest extends BaseRequest {

  private String username;
  private String password;
  private String openId;
  private boolean oneTimeUse=false;

  /**
   * @return True if this User will not have any identifying credentials other than a server-provided session UUID
   */
  public boolean isOneTimeUse() {
    return oneTimeUse;
  }

  public void setOneTimeUse(boolean oneTimeUse) {
    this.oneTimeUse = oneTimeUse;
  }

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
