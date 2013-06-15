package org.multibit.mbm.client.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.client.domain.model.model.Customer} is not found
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
