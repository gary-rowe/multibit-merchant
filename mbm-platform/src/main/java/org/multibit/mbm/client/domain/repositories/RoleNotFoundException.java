package org.multibit.mbm.client.domain.repositories;

/**
 * Thrown when a {@link org.multibit.mbm.client.domain.model.model.Customer} is not found
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException() {
    super();
  }

  public RoleNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
