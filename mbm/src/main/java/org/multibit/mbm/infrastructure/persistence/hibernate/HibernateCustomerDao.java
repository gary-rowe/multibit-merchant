package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.repositories.CustomerDao;
import org.multibit.mbm.domain.model.model.Customer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateCustomerDao")
public class HibernateCustomerDao extends BaseHibernateDao<Customer> implements CustomerDao {

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
}
