package org.multibit.mbm.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.domain.model.model.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
