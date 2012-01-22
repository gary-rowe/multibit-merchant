package org.multibit.mbm.customer.dao;

import org.multibit.mbm.customer.dto.Customer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("hibernateCustomerDao")
public class HibernateCustomerDao implements CustomerDao {

  @Resource(name="hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Customer getCustomerByOpenId(String openId) throws CustomerNotFoundException {
    List customers = hibernateTemplate.find("from Customer c where c.openId = ?", openId);
    if (customers==null || customers.isEmpty()) {
      throw new CustomerNotFoundException();
    }
    return (Customer) customers.get(0);
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
