package org.multibit.mbm.client.domain.repositories;

/**
 * Thrown when an {@link org.multibit.mbm.client.domain.model.model.Cart} is not found
 */
public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException() {
    super();
  }

  public CartNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
