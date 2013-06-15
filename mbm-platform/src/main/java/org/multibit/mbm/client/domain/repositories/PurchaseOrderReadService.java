package org.multibit.mbm.client.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.client.domain.model.model.PurchaseOrder;
import org.multibit.mbm.client.domain.model.model.Supplier;
import org.multibit.mbm.client.domain.repositories.common.EntityReadService;

public interface PurchaseOrderReadService extends EntityReadService<PurchaseOrder> {

  /**
   * Get the PurchaseOrder for the Supplier, initialising the {@link org.multibit.mbm.client.domain.model.model.PurchaseOrderItem}s
   *
   * @param supplier The owning Supplier
   *
   * @return A persistent delivery (never null)
   */
  Optional<PurchaseOrder> getInitialisedPurchaseOrderBySupplier(Supplier supplier);

  /**
   * Persist the given PurchaseOrder
   *
   * @param delivery A PurchaseOrder (either new or updated)
   *
   * @return The persisted PurchaseOrder
   */
  PurchaseOrder saveOrUpdate(PurchaseOrder delivery);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
