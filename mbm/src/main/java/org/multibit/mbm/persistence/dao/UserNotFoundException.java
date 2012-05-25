package org.multibit.mbm.persistence.dao;

/**
 * Thrown when a {@link org.multibit.mbm.persistence.dto.Customer} is not found
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
