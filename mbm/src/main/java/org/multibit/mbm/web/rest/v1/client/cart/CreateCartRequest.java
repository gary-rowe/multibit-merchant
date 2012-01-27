package org.multibit.mbm.web.rest.v1.client.cart;

import com.google.common.collect.Lists;
import org.multibit.mbm.web.rest.v1.client.BaseRequest;

import java.util.List;

/**
 *  <p>Request to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provision of client state to create a {@link org.multibit.mbm.cart.dto.Cart}</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CreateCartRequest extends BaseRequest {
  
  List<CartItemSummary> cartItemSummaries = Lists.newArrayList();

  public List<CartItemSummary> getCartItemSummaries() {
    return cartItemSummaries;
  }

  public void setCartItemSummaries(List<CartItemSummary> cartItemSummaries) {
    this.cartItemSummaries = cartItemSummaries;
  }
}
