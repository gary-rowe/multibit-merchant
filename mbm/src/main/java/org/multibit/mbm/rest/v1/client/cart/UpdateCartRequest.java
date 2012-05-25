package org.multibit.mbm.rest.v1.client.cart;

import org.multibit.mbm.persistence.dto.Cart;

/**
 *  <p>Request to provide the following to {@link org.multibit.mbm.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provision of state required to update an existing Cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class UpdateCartRequest {

  public UpdateCartRequest() {
  }

  /**
   * Utility constructor to populate the request based on an existing cart
   * @param cart
   */
  public UpdateCartRequest(Cart cart) {
  }
}
