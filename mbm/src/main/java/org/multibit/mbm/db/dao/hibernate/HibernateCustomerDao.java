package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dto.Customer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateCustomerDao")
public class HibernateCustomerDao extends BaseHibernateDao implements CustomerDao {

  @Override
  public Optional<Customer> getCustomerByOpenId(String openId) {
    List customers = hibernateTemplate.find("from Customer c where c.openId = ?", openId);

    return first(customers, Customer.class);
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


  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
