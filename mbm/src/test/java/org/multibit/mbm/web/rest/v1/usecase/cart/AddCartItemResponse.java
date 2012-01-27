package org.multibit.mbm.web.rest.v1.usecase.cart;

import org.multibit.mbm.web.rest.v1.client.BaseResponse;
import org.multibit.mbm.web.rest.v1.client.cart.CartItemSummary;

import java.util.List;

/**
 *  <p>Response to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provision of state for client response</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class AddCartItemResponse extends BaseResponse {

  private List<CartItemSummary> cartItemSummaries;

  public List<CartItemSummary> getCartItemSummaries() {
    return cartItemSummaries;
  }
}
