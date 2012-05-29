package org.multibit.mbm.db.dao;

/**
 * Thrown when a {@link org.multibit.mbm.db.dto.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
