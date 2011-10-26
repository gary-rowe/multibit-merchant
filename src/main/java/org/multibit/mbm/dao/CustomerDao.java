package org.multibit.mbm.dao;


import org.multibit.mbm.domain.Customer;

public interface CustomerDao {

  Customer getCustomerByOpenId(String uuid) throws CustomerNotFoundException;

  void newCustomer(Customer newCustomer);
}
