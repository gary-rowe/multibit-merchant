package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.Supplier;
import org.multibit.mbm.client.domain.repositories.SupplierReadService;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository("hibernateSupplierDao")
public class HibernateSupplierReadService extends BaseHibernateReadService<Supplier> implements SupplierReadService {

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

  @Override
  public Optional<Supplier> getById(Long id) {
    return getById(Supplier.class, id);
  }

  @Override
  public PaginatedList<Supplier> getPaginatedList(int pageSize, int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, Supplier.class);
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
