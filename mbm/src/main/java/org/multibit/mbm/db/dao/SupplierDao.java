package org.multibit.mbm.db.dao;

import com.google.common.base.Optional;
import org.multibit.mbm.core.model.Supplier;

public interface SupplierDao {

  /**
   * Attempt to locate the supplier
   *
   * @param id The id
   * @return A matching Supplier
   */
  Optional<Supplier> getSupplierById(Long id);

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
