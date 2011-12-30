package org.multibit.mbm.dao;

/**
 * Thrown when an {@link org.multibit.mbm.catalog.Item} is not found
 */
public class ItemNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ItemNotFoundException() {
    super();
  }

  public ItemNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
