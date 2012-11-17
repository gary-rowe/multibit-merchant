package org.multibit.mbm.api.response.hal.purchaseorder;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.PurchaseOrder;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.PurchaseOrder}:</p>
 * <ul>
 * <li>Creates representation of multiple PurchaseOrders for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminPurchaseOrderCollectionBridge extends BaseBridge<List<PurchaseOrder>> {

  private final SupplierPurchaseOrderBridge supplierPurchaseOrderBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminPurchaseOrderCollectionBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierPurchaseOrderBridge = new SupplierPurchaseOrderBridge(uriInfo, principal);
  }

  public Resource toResource(List<PurchaseOrder> deliveries) {
    ResourceFactory resourceFactory = getResourceFactory();

    Resource purchaseOrderList = resourceFactory.newResource(this.uriInfo.getRequestUri().toString());

    for (PurchaseOrder purchaseOrder : deliveries) {
      Resource purchaseOrderResource = supplierPurchaseOrderBridge.toResource(purchaseOrder);

      purchaseOrderResource.withProperty("id", purchaseOrder.getId())
      // End of build
      ;

      purchaseOrderList.withSubresource("/purchase-order/" + purchaseOrder.getId(), purchaseOrderResource);
    }

    return purchaseOrderList;

  }

}
