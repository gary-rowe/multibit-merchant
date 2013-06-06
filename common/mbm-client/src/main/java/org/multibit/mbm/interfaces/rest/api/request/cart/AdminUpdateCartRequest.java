package org.multibit.mbm.interfaces.rest.api.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to update the details of an existing Cart by an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class AdminUpdateCartRequest extends PublicUpdateCartRequest {

  /**
   * The ID of a particular cart
   */
  // TODO Consider if this is necessary
  @JsonProperty
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
