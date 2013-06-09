package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.repositories.CustomerReadService;
import org.multibit.mbm.domain.model.model.Customer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateCustomerDao")
public class HibernateCustomerReadService extends BaseHibernateReadService<Customer> implements CustomerReadService {

  @Override
  public Optional<Customer> getCustomerByOpenId(String openId) {
    List customers = hibernateTemplate.find("from Customer c where c.openId = ?", openId);

    return first(customers);
  }

  @Override
  public Customer saveOrUpdate(Customer customer) {
    hibernateTemplate.saveOrUpdate(customer);
    return customer;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }


  @Override
  protected Customer initialized(Customer entity) {
    return entity;
  }

  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }

  @Override
  public Optional<Customer> getById(Long id) {
    return getById(Customer.class, id);
  }

  @Override
  public PaginatedList<Customer> getPaginatedList(int pageSize, int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, Customer.class);
  }

  @Override
  public PaginatedList<Item> getPaginatedListByExample(int pageSize, int pageNumber, Customer example) {
    throw new IllegalStateException("Not yet supported");
  }
}
