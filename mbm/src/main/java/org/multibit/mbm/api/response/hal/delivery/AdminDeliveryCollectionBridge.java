package org.multibit.mbm.api.response.hal.delivery;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Delivery;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Delivery}:</p>
 * <ul>
 * <li>Creates representation of multiple Deliverys for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminDeliveryCollectionBridge extends BaseBridge<List<Delivery>> {

  private final SupplierDeliveryBridge supplierDeliveryBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminDeliveryCollectionBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierDeliveryBridge = new SupplierDeliveryBridge(uriInfo, principal);
  }

  public Resource toResource(List<Delivery> deliveries) {
    ResourceFactory resourceFactory = getResourceFactory();

    Resource deliveryList = resourceFactory.newResource(this.uriInfo.getRequestUri().toString());

    for (Delivery delivery : deliveries) {
      Resource deliveryResource = supplierDeliveryBridge.toResource(delivery);

      deliveryResource.withProperty("id", delivery.getId())
      // End of build
      ;

      deliveryList.withSubresource("/delivery/" + delivery.getId(), deliveryResource);
    }

    return deliveryList;

  }

}
