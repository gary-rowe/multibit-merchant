package org.multibit.mbm.core.model;

import com.google.common.base.Preconditions;
import org.joda.money.BigMoney;
import org.multibit.mbm.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for a Delivery Item (link between Delivery and Item)</li>
 * </ul>
 * The DeliveryItem describes the Items assigned to a particular Delivery and any additional properties that are
 * specific to the relationship (for example the quantity of a given Item in the Delivery).
 * </p>
 */
@Entity
@Table(name = "delivery_items")
@AssociationOverrides({
  @AssociationOverride(
    name = "primaryKey.item",
    joinColumns = @JoinColumn(name = "item_id")),
  @AssociationOverride(
    name = "primaryKey.delivery",
    joinColumns = @JoinColumn(name = "delivery_id"))
})
public class DeliveryItem implements Serializable {

  private static final long serialVersionUID = 389475903837482L;

  private DeliveryItemPk primaryKey = new DeliveryItemPk();

  @Column(name = "quantity", nullable = false)
  private int quantity = 0;

  @Column(name = "supplier_sku", nullable = true)
  private String supplierSKU = "";

  @Column(name = "supplier_gtin", nullable = true)
  private String supplierGTIN = "";

  @Column(name = "batch_reference", nullable = true)
  private String batchReference = "";

  /**
   * Default constructor required by Hibernate
   */
  public DeliveryItem() {
  }

  /**
   * Standard constructor with mandatory fields
   *
   * @param delivery required delivery
   * @param item     required item
   */
  public DeliveryItem(Delivery delivery, Item item) {
    Preconditions.checkNotNull(delivery, "delivery is required");
    Preconditions.checkNotNull(item, "item is required");
    primaryKey.setDelivery(delivery);
    primaryKey.setItem(item);
  }

  /**
   * @return Returns the primary key
   */
  @EmbeddedId
  public DeliveryItemPk getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(DeliveryItemPk primaryKey) {
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
   * @return Returns primaryKey.getDelivery()
   */
  @Transient
  public Delivery getDelivery() {
    return primaryKey.getDelivery();
  }

  /**
   * @return The quantity of the Item (measured in the smallest divisible unit)
   */
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return The Supplier SKU reference
   */
  public String getSupplierSKU() {
    return supplierSKU;
  }

  public void setSupplierSKU(String supplierSKU) {
    this.supplierSKU = supplierSKU;
  }

  /**
   * @return The Supplier GTIN reference
   */
  public String getSupplierGTIN() {
    return supplierGTIN;
  }

  public void setSupplierGTIN(String supplierGTIN) {
    this.supplierGTIN = supplierGTIN;
  }

  /**
   * @return The Supplier's batch reference for these Items in the Delivery
   */
  public String getBatchReference() {
    return batchReference;
  }

  public void setBatchReference(String batchReference) {
    this.batchReference = batchReference;
  }

  /**
   * @return The stock-keeping unit (from primary key)
   */
  @Transient
  public String getItemSKU() {
    return getItem().getSKU();
  }

  /**
   * The price subtotal is the (quantity * price)
   *
   * @return The price subtotal
   */
  @Transient
  public BigMoney getPriceSubtotal() {
    return getItem().getLocalPrice().multipliedBy(quantity);
  }

  /**
   * The tax subtotal is the (price subtotal * tax rate)
   *
   * @return The tax subtotal
   */
  @Transient
  public BigMoney getTaxSubtotal() {
    return getPriceSubtotal().multipliedBy(getItem().getTaxRate());
  }

  /**
   * The delivery item subtotal is the (price subtotal + tax subtotal)
   *
   * @return The delivery item subtotal
   */
  @Transient
  public BigMoney getDeliveryItemSubtotal() {
    return getPriceSubtotal().plus(getTaxSubtotal());
  }

  /**
   * Primary key class to set the Delivery and the Item as primary key in this many to many
   * relationship.
   */
  @Embeddable
  public static class DeliveryItemPk implements Serializable {

    private static final long serialVersionUID = 1L;
    private Item item;
    private Delivery delivery;

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
     * The associated Delivery
     *
     * @return Returns the Delivery
     */
    @ManyToOne(targetEntity = Delivery.class)
    public Delivery getDelivery() {
      return delivery;
    }

    public void setDelivery(Delivery delivery) {
      this.delivery = delivery;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final DeliveryItemPk other = (DeliveryItemPk) obj;

      return ObjectUtils.isEqual(
        delivery, other.delivery,
        item, other.item
      );
    }

    @Override
    public int hashCode() {
      return ObjectUtils.getHashCode(delivery, item);
    }

    @Override
    public String toString() {
      return String.format("DeliveryItemPk[delivery=%s, item=%s]]", delivery, item);
    }


  }
}
