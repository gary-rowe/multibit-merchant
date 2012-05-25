package org.multibit.mbm.persistence.dao;

/**
 * Thrown when an {@link org.multibit.mbm.persistence.dto.Cart} is not found
 */
public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException() {
    super();
  }

  public CartNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
