package org.multibit.mbm.client.domain.model.model;

import com.google.common.collect.Lists;
import org.joda.money.BigMoney;

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
public class PurchaseOrderBuilder {

  private Long id;
  private Supplier supplier;
  private List<AddPurchaseOrderItem> addPurchaseOrderItems = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static PurchaseOrderBuilder newInstance() {
    return new PurchaseOrderBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   *
   * @return The item instance
   */
  public PurchaseOrder build() {

    validateState();

    // PurchaseOrder is a DTO so requires a default constructor
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setId(id);
    purchaseOrder.setSupplier(supplier);

    // Add any items
    for (AddPurchaseOrderItem addPurchaseOrderItem : addPurchaseOrderItems) {
      addPurchaseOrderItem.applyTo(purchaseOrder);
    }

    isBuilt = true;

    return purchaseOrder;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("Build process is complete - no further changes can be made");
    }
    if (supplier == null) {
      throw new IllegalStateException("PurchaseOrder requires a Supplier");
    }
  }

  /**
   * Creates a PurchaseOrderItem entry based on the parameters
   *
   * @param id The ID
   *
   * @return The builder
   */
  public PurchaseOrderBuilder withId(Long id) {

    validateState();

    this.id = id;

    return this;
  }

  /**
   * Creates a PurchaseOrderItem entry based on the parameters
   *
   * @param item      The persistent Item (this should already exist in the database)
   * @param quantity  The quantity
   * @param unitPrice The price per unit
   * @param unitTax   The tax per unit
   *
   * @return The builder
   */
  public PurchaseOrderBuilder withPurchaseOrderItem(
    Item item,
    int quantity,
    BigMoney unitPrice,
    BigMoney unitTax) {

    validateState();

    addPurchaseOrderItems.add(
      new AddPurchaseOrderItem(
        item,
        quantity,
        unitPrice,
        unitTax));

    return this;
  }

  /**
   * Creates a PurchaseOrderItem entry based on the parameters
   *
   * @param purchaseOrderItem A PurchaseOrderItem
   *
   * @return The builder
   */
  public PurchaseOrderBuilder withPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {

    validateState();

    addPurchaseOrderItems.add(
      new AddPurchaseOrderItem(
        purchaseOrderItem.getItem(),
        purchaseOrderItem.getQuantity(),
        purchaseOrderItem.getUnitPrice(),
        purchaseOrderItem.getUnitTax()));

    return this;
  }

  /**
   * Creates multiple PurchaseOrderItem entries based on the parameters
   *
   * @param purchaseOrderItems A collection of PurchaseOrderItems (Item will be re-used)
   *
   * @return The builder
   */
  public PurchaseOrderBuilder withPurchaseOrderItems(Collection<PurchaseOrderItem> purchaseOrderItems) {

    validateState();

    for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
      withPurchaseOrderItem(purchaseOrderItem);
    }

    return this;
  }

  public PurchaseOrderBuilder withSupplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

  /**
   * Storage of parameters until ready for application
   */
  private class AddPurchaseOrderItem {
    private final Item item;
    private final int quantity;
    private final BigMoney unitPrice;
    private final BigMoney unitTax;

    AddPurchaseOrderItem(
      Item item,
      int quantity,
      BigMoney unitPrice,
      BigMoney unitTax) {
      this.item = item;
      this.quantity = quantity;
      this.unitPrice = unitPrice;
      this.unitTax = unitTax;
    }

    /**
     * Applies the parameters to the given PurchaseOrder
     *
     * @param purchaseOrder The PurchaseOrder
     */
    void applyTo(PurchaseOrder purchaseOrder) {

      // Bind the Item to the PurchaseOrder using the primary key
      PurchaseOrderItem.PurchaseOrderItemPk purchaseOrderItemPk = new PurchaseOrderItem.PurchaseOrderItemPk();
      purchaseOrderItemPk.setPurchaseOrder(purchaseOrder);
      purchaseOrderItemPk.setItem(item);

      PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
      purchaseOrderItem.setPrimaryKey(purchaseOrderItemPk);
      purchaseOrderItem.setQuantity(quantity);
      purchaseOrderItem.setUnitPrice(unitPrice);
      purchaseOrderItem.setUnitTax(unitTax);

      purchaseOrder.getPurchaseOrderItems().add(purchaseOrderItem);

    }
  }

}
