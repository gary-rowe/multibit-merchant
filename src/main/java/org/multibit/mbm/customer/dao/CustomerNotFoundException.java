package org.multibit.mbm.customer.dao;

/**
 * Thrown when a {@link org.multibit.mbm.customer.dto.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
