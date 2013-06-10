package org.multibit.mbm.domain.repositories;

import org.multibit.mbm.domain.model.model.Supplier;
import org.multibit.mbm.domain.repositories.common.EntityReadService;

public interface SupplierReadService extends EntityReadService<Supplier> {

  /**
   * Persist the given Supplier
   * @param supplier A Supplier (either new or updated)
   * @return The persisted Supplier
   */
  Supplier saveOrUpdate(Supplier supplier);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
