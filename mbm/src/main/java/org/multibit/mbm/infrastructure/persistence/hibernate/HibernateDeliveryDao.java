package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.multibit.mbm.domain.repositories.DeliveryDao;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.domain.model.model.DeliveryBuilder;
import org.multibit.mbm.domain.model.model.Supplier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernateDeliveryDao")
public class HibernateDeliveryDao extends BaseHibernateDao<Delivery> implements DeliveryDao {

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
  public List<Delivery> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<Delivery>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from Delivery");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
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
