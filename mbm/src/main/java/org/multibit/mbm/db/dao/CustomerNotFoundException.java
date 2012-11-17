package org.multibit.mbm.db.dao;

/**
 * Thrown when a {@link org.multibit.mbm.core.model.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
