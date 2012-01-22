package org.multibit.mbm.security.dao;

/**
 * Thrown when a {@link org.multibit.mbm.customer.dto.Customer} is not found
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
