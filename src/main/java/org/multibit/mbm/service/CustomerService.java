package org.multibit.mbm.service;

import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.domain.Customer;

public interface CustomerService {

    void haveBeenAuthenticated(String username, String email);

    Customer getCustomerFromOpenId(String openId) throws CustomerNotFoundException;
}
