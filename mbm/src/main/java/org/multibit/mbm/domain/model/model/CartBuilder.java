package org.multibit.mbm.domain.model.model;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 *  <p>Builder to provide the following to {@link Item}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartBuilder {

  private Long id;
  private Customer customer;
  private List<AddCartItem> addCartItems = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static CartBuilder newInstance() {
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
    cart.setId(id);
    cart.setCustomer(customer);

    // Add any items
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
    if (customer == null) {
      throw new IllegalStateException("Cart requires a Customer");
    }
  }

  /**
   * Creates a CartItem entry based on the parameters
   *
   * @param id The ID
   *
   * @return The builder
   */
  public CartBuilder withId(Long id) {

    validateState();

    this.id = id;

    return this;
  }

  /**
   * Creates a CartItem entry based on the parameters
   *
   * @param item     The persistent Item (this should already exist in the database)
   * @param quantity The quantity
   *
   * @return The builder
   */
  public CartBuilder withCartItem(Item item, int quantity) {

    validateState();

    addCartItems.add(new AddCartItem(item, quantity));

    return this;
  }

  /**
   * Creates a CartItem entry based on the parameters
   *
   * @param cartItem A CartItem
   *
   * @return The builder
   */
  public CartBuilder withCartItem(CartItem cartItem) {

    validateState();

    addCartItems.add(new AddCartItem(cartItem.getItem(), cartItem.getQuantity()));

    return this;
  }

  /**
   * Creates multiple CartItem entries based on the parameters
   *
   * @param cartItems A collection of CartItems (Item will be re-used)
   *
   * @return The builder
   */
  public CartBuilder withCartItems(Collection<CartItem> cartItems) {

    validateState();

    for (CartItem cartItem: cartItems) {
      addCartItems.add(new AddCartItem(cartItem.getItem(), cartItem.getQuantity()));
    }

    return this;
  }

  public CartBuilder withCustomer(Customer customer) {
    this.customer = customer;
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
