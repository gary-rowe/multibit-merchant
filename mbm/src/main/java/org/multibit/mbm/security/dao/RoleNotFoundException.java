package org.multibit.mbm.security.dao;

/**
 * Thrown when a {@link org.multibit.mbm.customer.dto.Customer} is not found
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException() {
    super();
  }

  public RoleNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
