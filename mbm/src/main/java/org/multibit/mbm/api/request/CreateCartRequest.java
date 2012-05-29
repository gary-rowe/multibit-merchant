package org.multibit.mbm.api.request;

import com.google.common.collect.Lists;
import org.multibit.mbm.api.CartItemSummary;

import java.util.List;

/**
 *  <p>Request to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 *  <ul>
 *  <li>Provision of client state to create a {@link org.multibit.mbm.db.dto.Cart}</li>
 *  </ul>
 *
 * @since 0.0.1
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
