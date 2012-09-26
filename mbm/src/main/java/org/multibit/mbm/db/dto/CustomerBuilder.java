package org.multibit.mbm.db.dto;

/**
 * <p>Builder to provide the following to {@link Customer}:</p>
 * <ul>
 * <li>Provide a fluent interface to facilitate building the entity</li>
 * </ul>
 * <h3>Note</h3>
 * <p>There is no User setting, since the User is the owner of the relationship
 * and thus handles the addition of the transient Customer.</p>
 *
 * @since 0.0.1
 *         
 */
public class CustomerBuilder {

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static CustomerBuilder newInstance() {
    return new CustomerBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public Customer build() {
    validateState();

    // Customer is a DTO and so requires a default constructor
    Customer customer = new Customer();

    Cart cart = CartBuilder
      .newInstance()
      .build();
    customer.setCart(cart);

    isBuilt = true;

    return customer;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

}
