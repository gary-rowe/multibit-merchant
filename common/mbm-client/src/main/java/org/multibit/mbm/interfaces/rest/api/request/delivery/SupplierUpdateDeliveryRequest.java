package org.multibit.mbm.interfaces.rest.api.request.delivery;

import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>Request to provide the following to {@link org.multibit.mbm.interfaces.rest.resources.DeliveryController}:</p>
 * <ul>
 * <li>Provision of state required to update an existing Delivery</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class SupplierUpdateDeliveryRequest {

  /**
   * The delivery items (item id, quantity etc)
   */
  @JsonProperty("delivery_items")
  List<SupplierDeliveryItem> deliveryItems = Lists.newArrayList();

  public List<SupplierDeliveryItem> getDeliveryItems() {
    return deliveryItems;
  }

  public void setDeliveryItems(List<SupplierDeliveryItem> deliveryItems) {
    this.deliveryItems = deliveryItems;
  }
}
