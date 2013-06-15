package org.multibit.mbm.interfaces.rest.api.representations.hal.delivery;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.interfaces.rest.api.hal.Representations;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

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

    Preconditions.checkNotNull(deliveries);

    URI self = UriBuilder.fromPath("/admin/delivery").build();
    Representation deliveryList = Representations.newPaginatedList(self, deliveries);

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
