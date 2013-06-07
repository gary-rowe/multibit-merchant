package org.multibit.mbm.domain.model.model;

import com.google.common.base.Preconditions;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.BigMoney;
import org.multibit.mbm.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for a PurchaseOrderItem (link between Purchase Order and Item)</li>
 * </ul>
 * The PurchaseOrderItem describes the Items assigned to a particular PurchaseOrder and any additional properties that are
 * specific to the relationship (for example the quantity of a given Item in the PurchaseOrder).
 * </p>
 */
@Entity
@Table(name = "purchase_order_items")
@AssociationOverrides({
  @AssociationOverride(
    name = "primaryKey.item",
    joinColumns = @JoinColumn(name = "item_id")),
  @AssociationOverride(
    name = "primaryKey.purchase_order",
    joinColumns = @JoinColumn(name = "purchase_order_id"))
})
public class PurchaseOrderItem implements Serializable {

  private static final long serialVersionUID = 389475903837482L;

  private PurchaseOrderItemPk primaryKey = new PurchaseOrderItemPk();

  @Column(name = "quantity", nullable = false)
  private int quantity = 0;

  @Columns(columns = {
    @Column(name = "unit_price_amount"),
    @Column(name = "unit_price_currency")
  })
  @Type(type = "org.multibit.mbm.infrastructure.persistence.hibernate.type.BigMoneyType")
  private BigMoney unitPrice;

  @Columns(columns = {
    @Column(name = "unit_tax_amount"),
    @Column(name = "unit_tax_currency")
  })
  @Type(type = "org.multibit.mbm.infrastructure.persistence.hibernate.type.BigMoneyType")
  private BigMoney unitTax;

  @Column(name = "supplier_sku", nullable = true)
  private String supplierSKU = "";

  @Column(name = "supplier_gtin", nullable = true)
  private String supplierGTIN = "";

  @Column(name = "batch_reference", nullable = true)
  private String batchReference = "";

  /**
   * Default constructor required by Hibernate
   */
  public PurchaseOrderItem() {
  }

  /**
   * Standard constructor with mandatory fields
   *
   * @param purchaseOrder required purchaseOrder
   * @param item          required item
   */
  public PurchaseOrderItem(PurchaseOrder purchaseOrder, Item item) {
    Preconditions.checkNotNull(purchaseOrder, "purchaseOrder is required");
    Preconditions.checkNotNull(item, "item is required");
    primaryKey.setPurchaseOrder(purchaseOrder);
    primaryKey.setItem(item);
  }

  /**
   * @return Returns the primary key
   */
  @EmbeddedId
  public PurchaseOrderItemPk getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(PurchaseOrderItemPk primaryKey) {
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
   * @return Returns primaryKey.getPurchaseOrder()
   */
  @Transient
  public PurchaseOrder getPurchaseOrder() {
    return primaryKey.getPurchaseOrder();
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
   * @return The Supplier's batch reference for these Items in the PurchaseOrder
   */
  public String getBatchReference() {
    return batchReference;
  }

  public void setBatchReference(String batchReference) {
    this.batchReference = batchReference;
  }

  /**
   * @return The unit price
   */
  public BigMoney getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigMoney unitPrice) {
    this.unitPrice = unitPrice;
  }

  /**
   * @return The unit tax
   */
  public BigMoney getUnitTax() {
    return unitTax;
  }

  public void setUnitTax(BigMoney unitTax) {
    this.unitTax = unitTax;
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
   * The purchaseOrder item subtotal is the (price subtotal + tax subtotal)
   *
   * @return The purchaseOrder item subtotal
   */
  @Transient
  public BigMoney getPurchaseOrderItemSubtotal() {
    return getPriceSubtotal().plus(getTaxSubtotal());
  }

  /**
   * Primary key class to set the PurchaseOrder and the Item as primary key in this many to many
   * relationship.
   */
  @Embeddable
  public static class PurchaseOrderItemPk implements Serializable {

    private static final long serialVersionUID = 1L;
    private Item item;
    private PurchaseOrder purchaseOrder;

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
     * The associated PurchaseOrder
     *
     * @return Returns the PurchaseOrder
     */
    @ManyToOne(targetEntity = PurchaseOrder.class)
    public PurchaseOrder getPurchaseOrder() {
      return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
      this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final PurchaseOrderItemPk other = (PurchaseOrderItemPk) obj;

      return ObjectUtils.isEqual(
        purchaseOrder, other.purchaseOrder,
        item, other.item
      );
    }

    @Override
    public int hashCode() {
      return ObjectUtils.getHashCode(purchaseOrder, item);
    }

    @Override
    public String toString() {
      return String.format("PurchaseOrderItemPk[purchaseOrder=%s, item=%s]]", purchaseOrder, item);
    }


  }
}
