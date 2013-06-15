package org.multibit.mbm.interfaces.rest.api.cart;

import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>Request to provide the following to {@link org.multibit.mbm.interfaces.rest.resources.CartController}:</p>
 * <ul>
 * <li>Provision of state required to update an existing Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class UpdateCartDto {

  /**
   * The cart items (item id, quantity etc)
   */
  @JsonProperty("cart_items")
  List<PublicCartItemDto> cartItems = Lists.newArrayList();

  public List<PublicCartItemDto> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<PublicCartItemDto> cartItems) {
    this.cartItems = cartItems;
  }
}
