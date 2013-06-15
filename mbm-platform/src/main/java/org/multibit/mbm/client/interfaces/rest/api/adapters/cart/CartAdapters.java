package org.multibit.mbm.client.interfaces.rest.api.adapters.cart;

import com.google.common.base.Preconditions;
import org.multibit.mbm.client.domain.model.model.Cart;
import org.multibit.mbm.client.interfaces.rest.api.adapters.cart.usecases.CopyCart;
import org.multibit.mbm.client.interfaces.rest.api.cart.CartDto;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to domain adapters</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartAdapters {

  /**
   * <p>Makes a copy of the Cart/CartItem/Item many-many relationship restricted to public properties.</p>
   *
   * @param cart The {@link Cart} providing the information
   *
   * @return A {@link CartDto}
   */
  public static CartDto copyCart(Cart cart) {
    Preconditions.checkNotNull(cart, "'cart' cannot be null");

    return new CopyCart().on(cart);
  }

}
