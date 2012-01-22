package org.multibit.mbm.cart.dto;

import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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

  @Transient
  private static final Logger log = LoggerFactory.getLogger(Cart.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  /**
   * A Cart has a single Customer
   */
  @OneToOne(mappedBy = "cart")
  private Customer customer = null;

  @OneToMany(targetEntity = CartItem.class, cascade = {CascadeType.ALL}, mappedBy = "primaryKey.cart")
  @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
  private Set<CartItem> cartItems = Sets.newLinkedHashSet();

  /*
  * Default constructor required for Hibernate
  */
  public Cart() {
  }

  /*
   * Mandatory field constructor required for builders
   */
  public Cart(Customer customer) {
    Assert.notNull(customer,"customer cannot be null");
    this.customer = customer;
    customer.setCart(this);
  }

  /**
   * @param item     The Item
   * @param quantity The quantity
   */
  @Transient
  public void setItemQuantity(Item item, int quantity) {
    Assert.notNull(item, "item cannot be null");

    CartItem cartItem = getCartItemByItem(item);

    if (quantity > 0) {
      // Insert or update Item without removal
      if (cartItem != null) {
        cartItem.setQuantity(quantity);
        log.debug("Update quantity to {}", cartItem.getQuantity());
      } else {
        log.debug("Insert new CartItem with quantity {}", quantity);
        cartItem = new CartItem(this, item);
        cartItem.setQuantity(quantity);
        cartItems.add(cartItem);
      }
    } else {
      if (cartItem != null) {
        // Remove Item
        cartItems.remove(cartItem);
      }
    }

  }

  /**
   * @param item The Item to search for
   *
   * @return The CartItem that contains the Item
   */
  @Transient
  public CartItem getCartItemByItem(Item item) {
    Assert.notNull(item, "item cannot be null");

    for (CartItem cartItem : cartItems) {
      if (cartItem.getItem().equals(item)) {
        return cartItem;
      }
    }
    return null;
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
   * @return The CartItems (contains the quantity, date etc)
   */
  public Set<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(Set<CartItem> cartItems) {
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
