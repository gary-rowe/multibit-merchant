package org.multibit.mbm.dao;

import org.multibit.mbm.domain.Customer;

public interface CustomerDao {

  /**
   * Attempt to locate the customer
   * @param openId The OpenId token
   * @return A matching Customer
   * @throws CustomerNotFoundException If something goes wrong
   */
  Customer getCustomerByOpenId(String openId) throws CustomerNotFoundException;

  /**
   * Add a new customer
   * @param newCustomer A transient Customer
   */
  void newCustomer(Customer newCustomer);
}
