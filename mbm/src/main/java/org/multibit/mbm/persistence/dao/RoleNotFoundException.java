package org.multibit.mbm.persistence.dao;

/**
 * Thrown when a {@link org.multibit.mbm.persistence.dto.Customer} is not found
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException() {
    super();
  }

  public RoleNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
