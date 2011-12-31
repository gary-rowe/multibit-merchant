package org.multibit.mbm.customer.builder;

import com.google.common.collect.Lists;
import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;

import java.util.List;

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

  private String openId;
  private List<AddContactMethod> addContactMethods = Lists.newArrayList();

  private boolean isBuilt= false;

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
    validateState();
    
    // Customer is a DTO and so requires a default constructor
    Customer customer = new Customer();

    customer.setOpenId(openId);

    for (AddContactMethod addContactMethod: addContactMethods) {
      addContactMethod.applyTo(customer);
    }

    isBuilt = true;
    
    return customer;
  }

  /**
   * @param openId The openId (e.g. "abc123")
   * @return The builder
   */
  public CustomerBuilder setOpenId(String openId) {
    validateState();
    this.openId=openId;
    return this;  
  }

  public CustomerBuilder addContactMethod(ContactMethod contactMethod, String detail) {
    addContactMethods.add(new AddContactMethod(contactMethod,detail));

    return this;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  /**
   * Handles adding a new contact method to the customer
   */
  private class AddContactMethod {
    private final ContactMethod contactMethod;
    private final String detail;
    
    private AddContactMethod(ContactMethod contactMethod, String detail) {
      this.contactMethod = contactMethod;
      this.detail = detail;
    }

    void applyTo(Customer customer) {
      ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
      contactMethodDetail.setPrimaryDetail(detail);

      customer.setContactMethodDetail(contactMethod,contactMethodDetail);

    }
  }

}
