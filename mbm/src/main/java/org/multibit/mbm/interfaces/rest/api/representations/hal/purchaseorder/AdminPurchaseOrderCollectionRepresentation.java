package org.multibit.mbm.interfaces.rest.api.representations.hal.purchaseorder;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.PurchaseOrder;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.PurchaseOrder}:</p>
 * <ul>
 * <li>Creates representation of multiple PurchaseOrders for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminPurchaseOrderCollectionRepresentation {

  public Representation get(PaginatedList<PurchaseOrder> deliveries) {
    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation purchaseOrderList = factory.newRepresentation();

    for (PurchaseOrder purchaseOrder : deliveries.list()) {
      Representation purchaseOrderRepresentation = new SupplierPurchaseOrderRepresentation().get(purchaseOrder);

      purchaseOrderRepresentation.withProperty("id", purchaseOrder.getId())
      // End of build
      ;

      purchaseOrderList.withRepresentation("/purchase-order/" + purchaseOrder.getId(), purchaseOrderRepresentation);
    }

    return purchaseOrderList;

  }

}
