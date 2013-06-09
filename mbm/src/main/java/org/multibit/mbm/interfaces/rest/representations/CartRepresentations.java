package org.multibit.mbm.interfaces.rest.representations;

import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.interfaces.rest.api.adapters.cart.CartAdapters;
import org.multibit.mbm.interfaces.rest.api.cart.CartDto;
import org.multibit.mbm.interfaces.rest.common.Representations;
import org.multibit.mbm.interfaces.rest.links.cart.CartLinks;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *         
 */
public class CartRepresentations {


  public static Representation retrieveOwnCart(Cart cart) {

    // Get the detail bean
    CartDto cartDto = CartAdapters.copyCart(cart);

    // Get supporting links
    URI self = CartLinks.self();
    Map<String, String> links = CartLinks.retrieveOwnCart(cart);

    // Get embedded content
    // CartItems
    Map<String, List<Representation>> embeddedMap = Maps.newHashMap();

    // Provide a representation to the client
    return Representations.asDetailWithEmbedded(self, cartDto, links, embeddedMap);

  }
}
