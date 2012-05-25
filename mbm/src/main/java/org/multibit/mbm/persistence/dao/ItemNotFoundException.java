package org.multibit.mbm.persistence.dao;

/**
 * Thrown when an {@link org.multibit.mbm.persistence.dto.Item} is not found
 */
public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException() {
    super();
  }

  public ItemNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
