package org.multibit.mbm.api.request.cart.purchaseorder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  <p>Value object to provide the following to {@link BuyerUpdatePurchaseOrderRequest}:</p>
 *  <ul>
 *  <li>Defines the updates to the PurchaseOrderItem</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class BuyerPurchaseOrderItem {

  @JsonProperty
  private String sku;

  @JsonProperty
  private int quantity;

  @JsonProperty
  private String unitPrice;

  @JsonProperty
  private String currencyCode;

  /**
   * Default constructor to allow request building
   */
  public BuyerPurchaseOrderItem() {
  }

  /**
   * Utility constructor for mandatory fields
   *
   * @param sku          The Stock Keeping Unit that is the public key
   * @param quantity     The quantity required of a particular unit
   * @param unitPrice    The unit price set by the Supplier
   * @param currencyCode The ISO currency code for the price
   */
  public BuyerPurchaseOrderItem(String sku, int quantity, String unitPrice, String currencyCode) {
    this.sku = sku;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.currencyCode = currencyCode;
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

  /**
   * @return The unit price
   */
  public String getUnitPrice() {
    return unitPrice;
  }

  /**
   * @return The currency code for the price
   */
  public String getCurrencyCode() {
    return currencyCode;
  }
}
