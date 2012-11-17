package org.multibit.mbm.db.dao;

/**
 * Thrown when a {@link org.multibit.mbm.core.model.Customer} is not found
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException() {
    super();
  }

  public RoleNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
