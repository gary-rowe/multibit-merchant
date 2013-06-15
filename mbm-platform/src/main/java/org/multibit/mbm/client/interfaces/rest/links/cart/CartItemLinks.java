package org.multibit.mbm.client.interfaces.rest.links.cart;

import org.multibit.mbm.client.domain.model.model.Cart;
import org.multibit.mbm.client.interfaces.rest.links.Links;

import java.net.URI;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to provide links between entities</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartItemLinks extends Links {

  public static final String PUBLIC_SELF_TEMPLATE = "/cart/item/{index}";
  public static final String ADMIN_SELF_TEMPLATE = "/admin/carts/{cartId}/item/{index}";

  /**
   * @return A URI suitable for use by the public (normally based on an inferred relationship without an ID)
   */
  public static URI self() {
    return createSelfUri(PUBLIC_SELF_TEMPLATE);
  }

  /**
   * @return A URI suitable for use by an admin (normally based on an ID)
   */
  public static URI adminSelf(Cart cart) {
    return createSelfUri(ADMIN_SELF_TEMPLATE, cart.getId().toString());
  }

}
