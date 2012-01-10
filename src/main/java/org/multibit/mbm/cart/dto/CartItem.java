package org.multibit.mbm.cart.dto;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.util.ObjectUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for a Cart Item (link between Cart and Item)</li>
 * </ul>
 * The CartItem describes the Items assigned to a particular Cart and any additional properties that are
 * specific to the relationship (for example the quantity of a given Item in the Cart).
 * </p>
 */
@Entity
@Table(name = "cart_items")
@AssociationOverrides( { @AssociationOverride(name = "primaryKey.item", joinColumns = @JoinColumn(name = "item_id")),
  @AssociationOverride(name = "primaryKey.cart", joinColumns = @JoinColumn(name = "cart_id")) })
public class CartItem implements Serializable {

  private static final long serialVersionUID = 389475903837482L;

  private CartItemPk           primaryKey       = new CartItemPk();

  @Column(name="quantity", nullable = false)
  private int quantity = 0;

  /**
   * Default constructor required by Hibernate
   */
  public CartItem() {}

  /**
   * Standard constructor with mandatory fields
   * @param cart required cart
   * @param item required item
   */
  public CartItem(Cart cart, Item item){
    Assert.notNull(cart, "cart is required");
    Assert.notNull(item, "item is required");
    primaryKey.setCart(cart);
    primaryKey.setItem(item);
  }

  /**
   * @return Returns the primary key
   */
  @EmbeddedId
  public CartItemPk getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(CartItemPk primaryKey) {
    this.primaryKey = primaryKey;
  }

  /**
   * @return Returns primaryKey.getItem()
   */
  @Transient
  public Item getItem() {
    return primaryKey.getItem();
  }

  /**
   * @return Returns primaryKey.getCart()
   */
  @Transient
  public Cart getCart() {
    return primaryKey.getCart();
  }

  /**
   *
   * @return The quantity of the Item (measured in the smallest divisible unit)
   */
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return The stock-keeping unit (from primary key)
   */
  @Transient
  public String getItemSKU(){
    return getItem().getSKU();
  }

  /**
   * Primary key class to set the Cart and the Item as primary key in this many to many
   * relationship.
   */
  @Embeddable
  public static class CartItemPk implements Serializable {

    private static final long serialVersionUID = 1L;
    private Item              item;
    private Cart              cart;

    /**
     * The associated Item
     *
     * @return returns the item
     */
    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "item_id")
    public Item getItem() {
      return item;
    }

    public void setItem(Item item) {
      this.item = item;
    }

    /**
     * The associated Cart
     *
     * @return Returns the Cart
     */
    @ManyToOne(targetEntity = Cart.class)
    public Cart getCart() {
      return cart;
    }

    public void setCart(Cart cart) {
      this.cart = cart;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final CartItemPk other = (CartItemPk) obj;

      return ObjectUtils.isEqual(
        cart, other.cart,
        item, other.item
      );
    }

    @Override
    public int hashCode() {
      return ObjectUtils.getHashCode(cart,item);
    }

    @Override
    public String toString() {
      return String.format("CartItemPk[cart=%s, item=%s]]", cart,item);
    }


  }
}
