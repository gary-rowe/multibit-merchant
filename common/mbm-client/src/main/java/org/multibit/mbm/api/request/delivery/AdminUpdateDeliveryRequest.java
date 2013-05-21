package org.multibit.mbm.api.request.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.multibit.mbm.api.request.delivery.SupplierUpdateDeliveryRequest;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to update the details of an existing Delivery by an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class AdminUpdateDeliveryRequest extends SupplierUpdateDeliveryRequest {

  /**
   * The ID of a particular delivery
   */
  // TODO Consider if this is necessary
  @JsonProperty
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
