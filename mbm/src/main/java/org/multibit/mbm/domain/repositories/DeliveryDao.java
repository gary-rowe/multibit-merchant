package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.domain.model.model.Supplier;

import java.util.List;

public interface DeliveryDao {

  /**
   * Attempt to locate the Delivery
   *
   * @param id The ID
   *
   * @return A matching Delivery
   */
  Optional<Delivery> getById(Long id);

  /**
   * Get the Delivery for the Supplier, initialising the {@link org.multibit.mbm.domain.model.model.DeliveryItem}s
   *
   * @param supplier The owning Supplier
   *
   * @return A persistent delivery (never null)
   */
  Optional<Delivery> getInitialisedDeliveryBySupplier(Supplier supplier);

  /**
   * Provide a paged list of all Deliverys
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  List<Delivery> getAllByPage(final int pageSize, final int pageNumber);

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
