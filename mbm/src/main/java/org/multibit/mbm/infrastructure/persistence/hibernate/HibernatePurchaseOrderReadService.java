package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.PurchaseOrder;
import org.multibit.mbm.domain.model.model.PurchaseOrderBuilder;
import org.multibit.mbm.domain.model.model.Supplier;
import org.multibit.mbm.domain.repositories.PurchaseOrderReadService;
import org.springframework.stereotype.Repository;

@Repository("hibernatePurchaseOrderDao")
public class HibernatePurchaseOrderReadService extends BaseHibernateReadService<PurchaseOrder> implements PurchaseOrderReadService {

  @SuppressWarnings("unchecked")
  @Override
  public Optional<PurchaseOrder> getById(Long id) {
    return getById(PurchaseOrder.class, id);
  }

  @Override
  public Optional<PurchaseOrder> getInitialisedPurchaseOrderBySupplier(Supplier supplier) {
    Preconditions.checkNotNull(supplier, "supplier cannot be null");

    PurchaseOrder purchaseOrder;
    if (supplier.getPurchaseOrders().isEmpty()) {
      // Create a suitable purchaseOrder
      purchaseOrder = PurchaseOrderBuilder
        .newInstance()
        .withSupplier(supplier)
        .build();
    } else {
      // TODO Need to differentiate between deliveries
      purchaseOrder = supplier.getPurchaseOrders().iterator().next();
    }

    // Ensure we associate the Supplier and PurchaseOrder with the current session and initialise the collection
    hibernateTemplate.saveOrUpdate(supplier);
    hibernateTemplate.initialize(supplier.getPurchaseOrders());

    return Optional.of(purchaseOrder);

  }

  /**
   * Initialize various collections since we are targeting the individual entity (perhaps for display)
   *
   * @param entity The entity
   *
   * @return The entity with all collections initialized
   */
  @Override
  protected PurchaseOrder initialized(PurchaseOrder entity) {
    return entity;
  }


  public PaginatedList<PurchaseOrder> getPaginatedList(final int pageSize, final int pageNumber) {

    return buildPaginatedList(pageSize, pageNumber, PurchaseOrder.class);
  }

  @Override
  public PurchaseOrder saveOrUpdate(PurchaseOrder purchaseOrder) {
    Preconditions.checkNotNull(purchaseOrder, "purchaseOrder cannot be null");
    hibernateTemplate.saveOrUpdate(purchaseOrder);
    return purchaseOrder;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }

}
