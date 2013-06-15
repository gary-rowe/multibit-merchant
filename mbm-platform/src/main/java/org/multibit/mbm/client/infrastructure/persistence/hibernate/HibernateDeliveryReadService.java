package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.Delivery;
import org.multibit.mbm.client.domain.model.model.DeliveryBuilder;
import org.multibit.mbm.client.domain.model.model.Supplier;
import org.multibit.mbm.client.domain.repositories.DeliveryReadService;
import org.springframework.stereotype.Repository;

@Repository("hibernateDeliveryDao")
public class HibernateDeliveryReadService extends BaseHibernateReadService<Delivery> implements DeliveryReadService {

  @SuppressWarnings("unchecked")
  @Override
  public Optional<Delivery> getById(Long id) {
    return getById(Delivery.class, id);
  }

  @Override
  public Optional<Delivery> getInitialisedDeliveryBySupplier(Supplier supplier) {
    Preconditions.checkNotNull(supplier, "supplier cannot be null");

    Delivery delivery;
    if (supplier.getDeliveries().isEmpty()) {
      // Create a suitable delivery
      delivery = DeliveryBuilder
        .newInstance()
        .withSupplier(supplier)
        .build();
    } else {
      // TODO Need to differentiate between deliveries
      delivery = supplier.getDeliveries().iterator().next();
    }

    // Ensure we associate the Supplier and Delivery with the current session and initialise the collection
    hibernateTemplate.saveOrUpdate(supplier);
    hibernateTemplate.initialize(supplier.getDeliveries());

    return Optional.of(delivery);

  }

  /**
   * Initialize various collections since we are targeting the individual entity (perhaps for display)
   *
   * @param entity The entity
   *
   * @return The entity with all collections initialized
   */
  @Override
  protected Delivery initialized(Delivery entity) {
    return entity;
  }


  @SuppressWarnings("unchecked")
  public PaginatedList<Delivery> getPaginatedList(final int pageSize, final int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, Delivery.class);
  }

  @Override
  public Delivery saveOrUpdate(Delivery delivery) {
    Preconditions.checkNotNull(delivery, "delivery cannot be null");
    hibernateTemplate.saveOrUpdate(delivery);
    return delivery;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }

}
