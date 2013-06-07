package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.repositories.SupplierDao;
import org.multibit.mbm.domain.model.model.Supplier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository("hibernateSupplierDao")
public class HibernateSupplierDao extends BaseHibernateDao<Supplier> implements SupplierDao {

  @Override
  public Optional<Supplier> getSupplierById(Long id) {
    return getById(Supplier.class, id);
  }

  @Override
  protected Supplier initialized(Supplier entity) {
    hibernateTemplate.initialize(entity.getDeliveries());
    return entity;
  }

  @Override
  public Supplier saveOrUpdate(Supplier supplier) {
    hibernateTemplate.saveOrUpdate(supplier);
    return supplier;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }


  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
