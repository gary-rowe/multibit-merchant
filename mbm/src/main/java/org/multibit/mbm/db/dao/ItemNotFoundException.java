package org.multibit.mbm.db.dao;

/**
 * Thrown when an {@link org.multibit.mbm.core.model.Item} is not found
 */
public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException() {
    super();
  }

  public ItemNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
