package org.multibit.mbm.cart.builder;

import com.google.common.collect.Lists;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.cart.dto.CartItem;
import org.multibit.mbm.catalog.dto.Item;

import java.util.List;

/**
 *  <p>Builder to provide the following to {@link org.multibit.mbm.catalog.dto.Item}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CartBuilder {

  private List<AddCartItem> addCartItems = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static CartBuilder getInstance() {
    return new CartBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   *
   * @return The item instance
   */
  public Cart build() {

    validateState();

    // Cart is a DTO so requires a default constructor
    Cart cart = new Cart();

    // Item is a DTO so requires a public default constructor
    for (AddCartItem addCartItem : addCartItems) {
      addCartItem.applyTo(cart);
    }

    isBuilt = true;

    return cart;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("Build process is complete - no further changes can be made");
    }
  }

  /**
   * Creates a CartItem entry based on the parameters
   * @param item The persistent Item (this should already exist in the database)
   * @param quantity The quantity
   * @return The builder
   */
  public CartBuilder addCartItem(Item item, int quantity) {

    validateState();

    addCartItems.add(new AddCartItem(item,quantity));

    return this;
  }

  /**
   * Storage of parameters until ready for application
   */
  private class AddCartItem {
    private final Item item;
    private final int quantity;

    AddCartItem(Item item, int quantity) {
      this.item = item;
      this.quantity = quantity;
    }

    /**
     * Applies the parameters to the given Cart
     *
     * @param cart The Cart
     */
    void applyTo(Cart cart) {

      // Bind the Item to the Cart using the primary key
      CartItem.CartItemPk cartItemPk = new CartItem.CartItemPk();
      cartItemPk.setCart(cart);
      cartItemPk.setItem(item);

      CartItem cartItem = new CartItem();
      cartItem.setPrimaryKey(cartItemPk);
      cartItem.setQuantity(quantity);
      
      cart.getCartItems().add(cartItem);

    }
  }

}
