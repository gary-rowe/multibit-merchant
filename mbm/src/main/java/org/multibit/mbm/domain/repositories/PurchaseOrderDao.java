package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.model.model.PurchaseOrder;
import org.multibit.mbm.domain.model.model.Supplier;

import java.util.List;

public interface PurchaseOrderDao {

  /**
   * Attempt to locate the PurchaseOrder
   *
   * @param id The ID
   *
   * @return A matching PurchaseOrder
   */
  Optional<PurchaseOrder> getById(Long id);

  /**
   * Get the PurchaseOrder for the Supplier, initialising the {@link org.multibit.mbm.domain.model.model.PurchaseOrderItem}s
   *
   * @param supplier The owning Supplier
   *
   * @return A persistent delivery (never null)
   */
  Optional<PurchaseOrder> getInitialisedPurchaseOrderBySupplier(Supplier supplier);

  /**
   * Provide a paged list of all PurchaseOrders
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  List<PurchaseOrder> getAllByPage(final int pageSize, final int pageNumber);

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
