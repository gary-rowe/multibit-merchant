package org.multibit.mbm.db.dto;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.multibit.mbm.utils.ObjectUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>DTO to provide the following to the application</p>
 * <ul>
 * <li>Provision of persistent state</li>
 * </ul>
 * <p>A Cart provides the mechanism for temporary storage of {@link Item}s. A {@link Customer} maintains a single Cart
 * (created on demand) </p>
 */
@Entity
@Table(name = "carts")
public class Cart implements Serializable {

  private static final long serialVersionUID = 38947590321234L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * A Cart has a single Customer
   */
  @OneToOne(mappedBy = "cart")
  private Customer customer = null;

  /**
   * A Cart has many CartItems that need to be eager to have meaning
   * They also have an order
   */
  @OneToMany(targetEntity = CartItem.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.cart",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  @OrderBy(value = "index")
  private List<CartItem> cartItems = Lists.newArrayList();

  /*
  * Default constructor required for Hibernate
  */
  public Cart() {
  }

  /*
   * Mandatory field constructor required for builders
   */
  public Cart(Customer customer) {
    Assert.notNull(customer, "customer cannot be null");
    this.customer = customer;
    customer.setCart(this);
  }

  /**
   * Handles the process of updating the Item quantities
   *
   * @param item     The Item (usually source from ItemDao)
   * @param quantity The quantity (>0 is add/update, otherwise remove)
   */
  @Transient
  public void setItemQuantity(Item item, int quantity) {
    Assert.notNull(item, "item cannot be null");

    Optional<CartItem> cartItemOptional = getCartItemByItem(item);

    if (quantity > 0) {
      if (cartItemOptional.isPresent()) {
        // Update
        cartItemOptional.get().setQuantity(quantity);
      } else {
        // Insert
        CartItem newCartItem = new CartItem(this, item);
        newCartItem.setIndex(cartItems.size());
        newCartItem.setQuantity(quantity);
        cartItems.add(newCartItem);
      }
    } else {
      if (cartItemOptional.isPresent()) {
        // Remove
        cartItems.remove(cartItemOptional.get());
        // Re-align the other cart item index values
        int i=0;
        for (CartItem cartItem : cartItems) {
          cartItem.setIndex(i++);
        }

      }
    }

  }

  /**
   * @param item The Item to search for
   *
   * @return The CartItem that contains the Item, or absent
   */
  @Transient
  public Optional<CartItem> getCartItemByItem(Item item) {
    Assert.notNull(item, "item cannot be null");

    for (CartItem cartItem : cartItems) {
      if (cartItem.getItem().equals(item)) {
        return Optional.of(cartItem);
      }
    }
    return Optional.absent();
  }

  /**
   * @return The number of separate items
   */
  @Transient
  public int getItemTotal() {
    return cartItems.size();
  }

  /**
   *
   * @return The total quantity of all items
   */
  @Transient
  public int getQuantityTotal() {
    int itemCount = 0;
    for (CartItem cartItem : cartItems) {
      itemCount += cartItem.getQuantity();
    }
    return itemCount;
  }

  /**
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The Customer that owns this Cart
   */
  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /**
   * @return The CartItems (contains the ordering index, quantity, date etc)
   */
  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Cart other = (Cart) obj;

    return ObjectUtils.isEqual(
      id, other.id
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id);
  }

  @Override
  public String toString() {
    return String.format("Cart[id=%s]]", id);
  }
}
