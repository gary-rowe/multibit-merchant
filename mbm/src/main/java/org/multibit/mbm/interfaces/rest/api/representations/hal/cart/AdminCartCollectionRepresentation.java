package org.multibit.mbm.interfaces.rest.api.representations.hal.cart;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Cart;

import java.util.List;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Cart}:</p>
 * <ul>
 * <li>Creates representation of multiple Carts for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminCartCollectionRepresentation {

  private final PublicCartRepresentation publicCartRepresentation = new PublicCartRepresentation();

  public Representation get(List<Cart> carts) {
    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation cartList = factory.newRepresentation();

    for (Cart cart : carts) {
      Representation cartRepresentation= publicCartRepresentation.get(cart);

      cartRepresentation.withProperty("id", cart.getId())
      // End of build
      ;

      cartList.withRepresentation("/cart/" + cart.getId(), cartRepresentation);
    }

    return cartList;

  }

}
