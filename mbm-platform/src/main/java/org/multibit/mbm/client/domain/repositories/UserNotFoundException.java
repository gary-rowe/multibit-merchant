package org.multibit.mbm.client.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.client.domain.model.model.Customer} is not found
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
