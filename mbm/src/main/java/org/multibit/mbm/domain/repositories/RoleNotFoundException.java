package org.multibit.mbm.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.domain.model.model.Customer} is not found
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException() {
    super();
  }

  public RoleNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
