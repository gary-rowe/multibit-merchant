package org.multibit.mbm.catalog.dao;

/**
 * Thrown when an {@link org.multibit.mbm.catalog.dto.Item} is not found
 */
public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException() {
    super();
  }

  public ItemNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
