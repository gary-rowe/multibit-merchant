package org.multibit.mbm.api.request;

import org.multibit.mbm.db.dto.Cart;

/**
 * <p>Request to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 * <ul>
 * <li>Provision of state required to update an existing Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CustomerUpdateCartRequest {

  public CustomerUpdateCartRequest() {
  }

  /**
   * Utility constructor to populate the request based on an existing cart
   *
   * @param cart The Cart
   */
  public CustomerUpdateCartRequest(Cart cart) {
  }
}
