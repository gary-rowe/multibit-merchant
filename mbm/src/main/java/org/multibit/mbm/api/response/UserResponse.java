package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.User;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>Response to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 * <ul>
 * <li>Provides the contents of a Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserResponse extends BaseResponse {

  @XmlElement
  @JsonProperty
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
    setSessionId(user.getUUID());
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
