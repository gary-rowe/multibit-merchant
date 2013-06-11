package org.multibit.mbm.interfaces.rest.api.representations.hal.delivery;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Delivery;

import java.util.List;

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

  public Representation get(List<Delivery> deliveries) {
    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation deliveryList = factory.newRepresentation();

    for (Delivery delivery : deliveries) {
      Representation deliveryRepresentation = supplierDeliveryRepresentation.get(delivery);

      deliveryRepresentation.withProperty("id", delivery.getId())
      // End of build
      ;

      deliveryList.withRepresentation("/delivery/" + delivery.getId(), deliveryRepresentation);
    }

    return deliveryList;

  }

}
