package org.multibit.mbm.api.request.cart;

import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>Request to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 * <ul>
 * <li>Provision of state required to update an existing Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicUpdateCartRequest {

  /**
   * The cart items (item id, quantity etc)
   */
  @JsonProperty("cart_items")
  List<PublicCartItem> cartItems = Lists.newArrayList();

  public List<PublicCartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<PublicCartItem> cartItems) {
    this.cartItems = cartItems;
  }
}
