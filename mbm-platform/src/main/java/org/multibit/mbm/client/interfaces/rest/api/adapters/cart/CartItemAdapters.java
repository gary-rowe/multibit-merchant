package org.multibit.mbm.client.interfaces.rest.api.adapters.cart;

import com.google.common.base.Preconditions;
import org.multibit.mbm.client.domain.model.model.CartItem;
import org.multibit.mbm.client.interfaces.rest.api.adapters.cart.usecases.CopyCartItem;
import org.multibit.mbm.client.interfaces.rest.api.cart.CartItemDto;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to domain adapters</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartItemAdapters {

  /**
   *
   * @param cartItem Copy a {@link CartItem} to a {@link CartItemDto}
   * @return A {@link CartItemDto}
   */
  public static CartItemDto copyCartItem(CartItem cartItem) {
    Preconditions.checkNotNull(cartItem, "'cartItem' cannot be null");

    return new CopyCartItem().on(cartItem);
  }
}
