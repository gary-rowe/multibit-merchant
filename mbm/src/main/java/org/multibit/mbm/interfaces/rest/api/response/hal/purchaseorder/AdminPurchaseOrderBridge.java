package org.multibit.mbm.interfaces.rest.api.response.hal.purchaseorder;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.domain.model.model.PurchaseOrder;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to administrators:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.domain.model.model.PurchaseOrder} for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminPurchaseOrderBridge extends BaseBridge<PurchaseOrder> {

  private final SupplierPurchaseOrderBridge supplierPurchaseOrderBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public AdminPurchaseOrderBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierPurchaseOrderBridge = new SupplierPurchaseOrderBridge(uriInfo,principal);
  }

  public Resource toResource(PurchaseOrder purchaseOrder) {
    ResourceAsserts.assertNotNull(purchaseOrder,"purchaseOrder");
    ResourceAsserts.assertNotNull(purchaseOrder.getId(),"id");

    // Build on the Customer representation
    Resource purchaseOrderResource = supplierPurchaseOrderBridge.toResource(purchaseOrder)
      // Must use individual property entries due to collections
      .withProperty("id", purchaseOrder.getId())
      // End of build
      ;

    return purchaseOrderResource;

  }

}
