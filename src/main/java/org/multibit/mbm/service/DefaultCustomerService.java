package org.multibit.mbm.service;

import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.domain.Customer;
import org.multibit.mbm.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService implements CustomerService {

  @Autowired
  private CustomerDao customerDao;
  @Autowired
  private IdGenerator idGenerator;

  @Override
  public void haveBeenAuthenticated(String openId) {
    try {
      // Is this a new customer?
      customerDao.getCustomerByOpenId(openId);
    } catch (CustomerNotFoundException ex) {
      // Yes, so add them in
      Customer newCustomer = new Customer(idGenerator.random(), openId);
      customerDao.newCustomer(newCustomer);
    }

  }

  @Override
  public Customer getCustomerFromOpenId(String openId) throws CustomerNotFoundException {
    return customerDao.getCustomerByOpenId(openId);
  }
}
