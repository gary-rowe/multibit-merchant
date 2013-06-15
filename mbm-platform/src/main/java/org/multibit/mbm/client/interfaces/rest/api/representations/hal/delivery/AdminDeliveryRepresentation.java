package org.multibit.mbm.client.interfaces.rest.api.representations.hal.delivery;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.client.domain.model.model.Delivery;

/**
 * <p>Representation to provide the following to administrators:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.client.domain.model.model.Delivery} for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminDeliveryRepresentation {

  private final SupplierDeliveryRepresentation supplierDeliveryRepresentation = new SupplierDeliveryRepresentation();

  public Representation get(Delivery delivery) {
    Preconditions.checkNotNull(delivery, "delivery");
    Preconditions.checkNotNull(delivery.getId(), "id");

    // Build on the Customer representation
    Representation userRepresentation = supplierDeliveryRepresentation.get(delivery)
      // Must use individual property entries due to collections
      .withProperty("id", delivery.getId())
      // End of build
      ;

    return userRepresentation;

  }

}
