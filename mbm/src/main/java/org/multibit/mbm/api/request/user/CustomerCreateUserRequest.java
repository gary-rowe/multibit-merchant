package org.multibit.mbm.api.request.user;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>Request to provide the following to controllers:</p>
 * <ul>
 * <li>Provision of client state to create an initial bare bones User by a Customer</li>
 * </ul>
 * <p>Note that subsequent updates to the User can set more detail into the User as required</p>
 * <p>A Customer created User contains the bare minimum information</p>
 *
 * @since 0.0.1
 *         
 */
public class CustomerCreateUserRequest extends BaseUserRequest {

  @JsonProperty
  private boolean oneTimeUse = false;

  /**
   * @return True if this User will not have any identifying credentials other than a server-provided session UUID
   */
  public boolean isOneTimeUse() {
    return oneTimeUse;
  }

  public void setOneTimeUse(boolean oneTimeUse) {
    this.oneTimeUse = oneTimeUse;
  }

}
