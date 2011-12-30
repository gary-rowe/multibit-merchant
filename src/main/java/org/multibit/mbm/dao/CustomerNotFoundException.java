package org.multibit.mbm.dao;

/**
 * Thrown when a {@link org.multibit.mbm.customer.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
