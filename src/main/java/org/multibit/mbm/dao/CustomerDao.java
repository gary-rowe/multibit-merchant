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
   * Persist the given Customer
   * @param newCustomer A Customer (either new or updated)
   */
  void persist(Customer newCustomer);

}
