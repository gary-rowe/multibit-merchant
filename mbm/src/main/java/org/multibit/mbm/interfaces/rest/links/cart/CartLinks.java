package org.multibit.mbm.interfaces.rest.links.cart;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.interfaces.rest.links.Links;
import org.multibit.mbm.interfaces.rest.links.user.CustomerLinks;

import java.net.URI;
import java.util.Map;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to provide links between entities</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartLinks extends Links {

  public static final String PUBLIC_SELF_TEMPLATE = "/cart";

  public static final String ADMIN_PATH="/admin/carts";
  public static final String ADMIN_SELF_TEMPLATE = "/admin/carts/{id}";

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

  /**
   * @param cart The domain object providing the information
   *
   * @return A map of links suitable for this use case
   */
  public static Map<String, String> retrieveOwnCart(Cart cart) {
    Preconditions.checkNotNull(cart, "'cart' cannot be null");

    Map<String, String> links = Maps.newHashMap();

    URI customer = CustomerLinks.self(cart.getCustomer());

    links.put("customer", customer.toString());

    return links;
  }

}
