package org.multibit.mbm.customer.builder;

import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;

/**
 *  <p>Builder to provide the following to {@link Customer}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CustomerBuilder {

  private Customer customer =null;

  /**
   * @return A new instance of the builder
   */
  public static CustomerBuilder getInstance() {
    return new CustomerBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public Customer build() {
    return customer;
  }

  /**
   * Create a new {@link org.multibit.mbm.catalog.dto.Item}
   * @return The builder
   */
  public CustomerBuilder newCustomer() {
    this.customer = new Customer();
    return this;
  }

  /**
   * 
   * @param openId The openId (e.g. "abc123")
   * @return The builder
   */
  public CustomerBuilder setOpenId(String openId) {
    if (customer ==null) {
      throw new IllegalStateException("Customer is null, need to call newCustomer() first.");
    }
    this.customer.setOpenId(openId);

    return this;  
  }

  public CustomerBuilder addContactMethod(ContactMethod contactMethod, String detail) {
    if (customer ==null) {
      throw new IllegalStateException("Customer is null, need to call newCustomer() first.");
    }

    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail(detail);

    this.customer.setContactMethodDetail(contactMethod,contactMethodDetail);

    return this;
  }
}
