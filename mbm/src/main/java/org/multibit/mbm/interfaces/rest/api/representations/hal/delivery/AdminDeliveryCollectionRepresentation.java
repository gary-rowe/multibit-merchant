package org.multibit.mbm.interfaces.rest.api.representations.hal.delivery;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Delivery;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Delivery}:</p>
 * <ul>
 * <li>Creates representation of multiple Deliveries for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminDeliveryCollectionRepresentation {

  private final SupplierDeliveryRepresentation supplierDeliveryRepresentation = new SupplierDeliveryRepresentation();

  public Representation get(PaginatedList<Delivery> deliveries) {
    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation deliveryList = factory.newRepresentation();

    for (Delivery delivery : deliveries.list()) {
      Representation deliveryRepresentation = supplierDeliveryRepresentation.get(delivery);

      deliveryRepresentation.withProperty("id", delivery.getId())
      // End of build
      ;

      deliveryList.withRepresentation("/delivery/" + delivery.getId(), deliveryRepresentation);
    }

    return deliveryList;

  }

}
