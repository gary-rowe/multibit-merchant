package org.multibit.mbm.interfaces.rest.api.representations.hal.purchaseorder;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.model.model.PurchaseOrder;

/**
 * <p>Representation to provide the following to administrators:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.domain.model.model.PurchaseOrder} for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminPurchaseOrderRepresentation {

  public Representation get(PurchaseOrder purchaseOrder) {
    Preconditions.checkNotNull(purchaseOrder, "purchaseOrder");
    Preconditions.checkNotNull(purchaseOrder.getId(), "id");

    // Build on the supplier purchase order representation
    return new SupplierPurchaseOrderRepresentation().get(purchaseOrder)
      // Must use individual property entries due to collections
      .withProperty("id", purchaseOrder.getId());

  }

}
