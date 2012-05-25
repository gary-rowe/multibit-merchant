package org.multibit.mbm.persistence.dao;

/**
 * Thrown when a {@link org.multibit.mbm.persistence.dto.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
