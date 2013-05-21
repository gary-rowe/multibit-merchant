package org.multibit.mbm.api.request.cart.purchaseorder;

import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>Request to provide the following to purchase order resources:</p>
 * <ul>
 * <li>Provision of state required to update an existing PurchaseOrder</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class BuyerUpdatePurchaseOrderRequest {

  /**
   * The purchase order items (item id, quantity etc)
   */
  @JsonProperty("purchase_order_items")
  List<BuyerPurchaseOrderItem> purchaseOrderItems = Lists.newArrayList();

  public List<BuyerPurchaseOrderItem> getPurchaseOrderItems() {
    return purchaseOrderItems;
  }

  public void setPurchaseOrderItems(List<BuyerPurchaseOrderItem> purchaseOrderItems) {
    this.purchaseOrderItems = purchaseOrderItems;
  }
}
