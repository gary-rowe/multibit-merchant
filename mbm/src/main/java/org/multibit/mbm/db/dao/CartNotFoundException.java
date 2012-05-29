package org.multibit.mbm.db.dao;

/**
 * Thrown when an {@link org.multibit.mbm.db.dto.Cart} is not found
 */
public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException() {
    super();
  }

  public CartNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
