package org.multibit.mbm.service;

import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.domain.Customer;

public interface CustomerService {

  /**
   * <p>Attempts to insert new Customers into the database in response to a successful login</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   */
  void haveBeenAuthenticated(String openId);

  /**
   * <p>Attempt to get the Customer using an OpenId</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   *
   * @return The Customer
   *
   * @throws CustomerNotFoundException If not found
   */
  Customer getCustomerFromOpenId(String openId) throws CustomerNotFoundException;

}
