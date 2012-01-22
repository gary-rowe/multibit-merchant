package org.multibit.mbm.customer.builder;

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

    // TODO Add support for building Carts etc otherwise this builder has no purpose

    isBuilt = true;
    
    return customer;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

}
