package org.multibit.mbm.domain.repositories;

/**
 * Thrown when an {@link org.multibit.mbm.domain.model.model.Item} is not found
 */
public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException() {
    super();
  }

  public ItemNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
