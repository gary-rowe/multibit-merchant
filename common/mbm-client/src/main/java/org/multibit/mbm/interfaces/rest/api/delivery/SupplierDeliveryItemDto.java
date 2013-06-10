package org.multibit.mbm.interfaces.rest.api.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  <p>Value object to provide the following to {@link SupplierUpdateDeliveryDto}:</p>
 *  <ul>
 *  <li>Defines the updates to the DeliveryItem</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class SupplierDeliveryItemDto {

  @JsonProperty
  private String sku;

  @JsonProperty
  private int quantity;

  /**
   * Default constructor to allow request building
   */
  public SupplierDeliveryItemDto() {
  }

  /**
   * Utility constructor for mandatory fields
   *
   * @param sku      The Stock Keeping Unit that is the public key
   * @param quantity The quantity required
   */
  public SupplierDeliveryItemDto(String sku, int quantity) {
    this.sku = sku;
    this.quantity = quantity;
  }

  /**
   * @return The SKU
   */
  public String getSKU() {
    return sku;
  }

  /**
   * @return The unit quantity
   */
  public int getQuantity() {
    return quantity;
  }
}
