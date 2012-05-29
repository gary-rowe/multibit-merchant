package org.multibit.mbm.db.dao;

/**
 * Thrown when a {@link org.multibit.mbm.db.dto.Customer} is not found
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
