package org.multibit.mbm.interfaces.rest.api.representations.hal.cart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.domain.model.model.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminCartRepresentation {

  private final PublicCartRepresentation customerCartRepresentation = new PublicCartRepresentation();

  public Representation get(Cart cart) {
    Preconditions.checkNotNull(cart, "cart");
    Preconditions.checkNotNull(cart.getId(), "id");

    // Build on the Customer representation
    return customerCartRepresentation.get(cart)
      // Must use individual property entries due to collections
      .withProperty("id", cart.getId());

  }

}
