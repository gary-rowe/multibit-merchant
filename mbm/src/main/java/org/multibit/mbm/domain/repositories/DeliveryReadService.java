package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.domain.model.model.Supplier;
import org.multibit.mbm.domain.repositories.common.EntityReadService;

public interface DeliveryReadService extends EntityReadService<Delivery> {

  /**
   * Get the Delivery for the Supplier, initialising the {@link org.multibit.mbm.domain.model.model.DeliveryItem}s
   *
   * @param supplier The owning Supplier
   *
   * @return A persistent delivery (never null)
   */
  Optional<Delivery> getInitialisedDeliveryBySupplier(Supplier supplier);

  /**
   * Persist the given Delivery
   *
   * @param delivery A Delivery (either new or updated)
   *
   * @return The persisted Delivery
   */
  Delivery saveOrUpdate(Delivery delivery);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
