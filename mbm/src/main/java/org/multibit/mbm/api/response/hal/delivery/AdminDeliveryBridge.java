package org.multibit.mbm.api.response.hal.delivery;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Delivery;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to administrators:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.db.dto.Delivery} for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminDeliveryBridge extends BaseBridge<Delivery> {

  private final SupplierDeliveryBridge supplierDeliveryBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminDeliveryBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierDeliveryBridge = new SupplierDeliveryBridge(uriInfo,principal);
  }

  public Resource toResource(Delivery delivery) {
    ResourceAsserts.assertNotNull(delivery,"delivery");
    ResourceAsserts.assertNotNull(delivery.getId(),"id");

    // Build on the Customer representation
    Resource userResource = supplierDeliveryBridge.toResource(delivery)
      // Must use individual property entries due to collections
      .withProperty("id", delivery.getId())
      // End of build
      ;

    return userResource;

  }

}
