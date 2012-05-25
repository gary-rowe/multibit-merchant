package org.multibit.mbm.persistence.dao.hibernate;

import org.multibit.mbm.persistence.dao.CustomerNotFoundException;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.persistence.dao.CustomerDao;
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
