package org.multibit.mbm.db.dto;

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
public class DeliveryBuilder {

  private Long id;
  private Supplier supplier;
  private List<AddDeliveryItem> addDeliveryItems = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static DeliveryBuilder newInstance() {
    return new DeliveryBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   *
   * @return The item instance
   */
  public Delivery build() {

    validateState();

    // Delivery is a DTO so requires a default constructor
    Delivery delivery = new Delivery();
    delivery.setId(id);
    delivery.setSupplier(supplier);

    // Add any items
    for (AddDeliveryItem addDeliveryItem : addDeliveryItems) {
      addDeliveryItem.applyTo(delivery);
    }

    isBuilt = true;

    return delivery;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("Build process is complete - no further changes can be made");
    }
    if (supplier == null) {
      throw new IllegalStateException("Delivery requires a Supplier");
    }
  }

  /**
   * Creates a DeliveryItem entry based on the parameters
   *
   * @param id The ID
   *
   * @return The builder
   */
  public DeliveryBuilder withId(Long id) {

    validateState();

    this.id = id;

    return this;
  }

  /**
   * Creates a DeliveryItem entry based on the parameters
   *
   * @param item     The persistent Item (this should already exist in the database)
   * @param quantity The quantity
   *
   * @return The builder
   */
  public DeliveryBuilder withDeliveryItem(Item item, int quantity) {

    validateState();

    addDeliveryItems.add(new AddDeliveryItem(item, quantity));

    return this;
  }

  /**
   * Creates a DeliveryItem entry based on the parameters
   *
   * @param deliveryItem A DeliveryItem
   *
   * @return The builder
   */
  public DeliveryBuilder withDeliveryItem(DeliveryItem deliveryItem) {

    validateState();

    addDeliveryItems.add(new AddDeliveryItem(deliveryItem.getItem(), deliveryItem.getQuantity()));

    return this;
  }

  /**
   * Creates multiple DeliveryItem entries based on the parameters
   *
   * @param deliveryItems A collection of DeliveryItems (Item will be re-used)
   *
   * @return The builder
   */
  public DeliveryBuilder withDeliveryItems(Collection<DeliveryItem> deliveryItems) {

    validateState();

    for (DeliveryItem deliveryItem: deliveryItems) {
      addDeliveryItems.add(new AddDeliveryItem(deliveryItem.getItem(), deliveryItem.getQuantity()));
    }

    return this;
  }

  public DeliveryBuilder withSupplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

  /**
   * Storage of parameters until ready for application
   */
  private class AddDeliveryItem {
    private final Item item;
    private final int quantity;

    AddDeliveryItem(Item item, int quantity) {
      this.item = item;
      this.quantity = quantity;
    }

    /**
     * Applies the parameters to the given Delivery
     *
     * @param delivery The Delivery
     */
    void applyTo(Delivery delivery) {

      // Bind the Item to the Delivery using the primary key
      DeliveryItem.DeliveryItemPk deliveryItemPk = new DeliveryItem.DeliveryItemPk();
      deliveryItemPk.setDelivery(delivery);
      deliveryItemPk.setItem(item);

      DeliveryItem deliveryItem = new DeliveryItem();
      deliveryItem.setPrimaryKey(deliveryItemPk);
      deliveryItem.setQuantity(quantity);

      delivery.getDeliveryItems().add(deliveryItem);

    }
  }

}
