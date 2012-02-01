package org.multibit.mbm.web.rest.v1.client.security;

import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.web.rest.v1.client.BaseResponse;
import org.springframework.util.Assert;

/**
 *  <p>Response to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provides the contents of a Cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class UserResponse extends BaseResponse {

  private String username = "";

  /**
   * Default constructor
   */
  public UserResponse() {
  }

  /**
   * @param user The User to base the summary upon
   */
  public UserResponse(User user) {
    Assert.notNull(user, "user must not be null");
    username = user.getUsername();
    sessionId = user.getUUID();
  }

  /**
   * @return The username
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
