package org.multibit.mbm.web.rest.v1.client.cart;

import org.multibit.mbm.cart.dto.Cart;

/**
 *  <p>Request to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
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
