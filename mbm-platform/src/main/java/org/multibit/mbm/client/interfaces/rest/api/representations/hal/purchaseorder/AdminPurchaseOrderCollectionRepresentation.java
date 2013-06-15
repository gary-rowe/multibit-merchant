package org.multibit.mbm.client.interfaces.rest.api.representations.hal.purchaseorder;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.PurchaseOrder;
import org.multibit.mbm.client.interfaces.rest.api.hal.Representations;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.client.domain.model.model.PurchaseOrder}:</p>
 * <ul>
 * <li>Creates representation of multiple PurchaseOrders for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminPurchaseOrderCollectionRepresentation {

  public Representation get(PaginatedList<PurchaseOrder> purchaseOrders) {

    Preconditions.checkNotNull(purchaseOrders);

    URI self = UriBuilder.fromPath("/admin/purchase-order").build();
    Representation purchaseOrderList = Representations.newPaginatedList(self, purchaseOrders);

    for (PurchaseOrder purchaseOrder : purchaseOrders.list()) {
      Representation purchaseOrderRepresentation = new SupplierPurchaseOrderRepresentation().get(purchaseOrder);

      purchaseOrderRepresentation.withProperty("id", purchaseOrder.getId())
      // End of build
      ;

      purchaseOrderList.withRepresentation("/purchase-order/" + purchaseOrder.getId(), purchaseOrderRepresentation);
    }

    return purchaseOrderList;

  }

}
