package org.multibit.mbm.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.domain.model.model.Customer} is not found
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
