package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.multibit.mbm.db.dao.PurchaseOrderDao;
import org.multibit.mbm.db.dto.PurchaseOrder;
import org.multibit.mbm.db.dto.PurchaseOrderBuilder;
import org.multibit.mbm.db.dto.Supplier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernatePurchaseOrderDao")
public class HibernatePurchaseOrderDao extends BaseHibernateDao<PurchaseOrder> implements PurchaseOrderDao {

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


  @SuppressWarnings("unchecked")
  public List<PurchaseOrder> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<PurchaseOrder>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from PurchaseOrder");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
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
