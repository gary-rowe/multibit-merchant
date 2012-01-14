package org.multibit.mbm.cart.dao;

/**
 * Thrown when an {@link org.multibit.mbm.cart.dto.Cart} is not found
 */
public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException() {
    super();
  }

  public CartNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
