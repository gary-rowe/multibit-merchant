package org.multibit.mbm.client.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.client.domain.model.model.Customer;
import org.multibit.mbm.client.domain.repositories.common.EntityReadService;

public interface CustomerReadService extends EntityReadService<Customer> {

  /**
   * Attempt to locate the customer
   *
   * @param openId The OpenId token
   * @return A matching Customer
   * @throws CustomerNotFoundException If something goes wrong
   */
  Optional<Customer> getCustomerByOpenId(String openId) throws CustomerNotFoundException;

  /**
   * Persist the given Customer
   * @param customer A Customer (either new or updated)
   * @return The persisted Customer
   */
  Customer saveOrUpdate(Customer customer);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
