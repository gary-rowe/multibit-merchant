package org.multibit.mbm.service;

import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.domain.Customer;
import org.multibit.mbm.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>Service to provide the following to Controllers:</p>
 * <ul>
 * <li>Transactional collection of Customer entries</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */@Service
public class CustomerService {

  @Resource(name= "hibernateCustomerDao")
  private CustomerDao customerDao;
  @Autowired
  private IdGenerator idGenerator;


  /**
   * <p>Attempts to insert new Customers into the database in response to a successful login</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   */
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

    /**
   * <p>Attempt to get the Customer using an OpenId</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   *
   * @return The Customer
   *
   * @throws CustomerNotFoundException If not found
   */
  public Customer getCustomerFromOpenId(String openId) throws CustomerNotFoundException {
    return customerDao.getCustomerByOpenId(openId);
  }
}
