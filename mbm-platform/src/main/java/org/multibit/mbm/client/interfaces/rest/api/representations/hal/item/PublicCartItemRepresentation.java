package org.multibit.mbm.client.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.client.domain.model.model.CartItem;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.client.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of a CartItem for the anonymous public</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicCartItemRepresentation {

  private final PublicItemRepresentation publicItemRepresentation = new PublicItemRepresentation();

  public Representation get(CartItem cartItem) {

    Preconditions.checkNotNull(cartItem, "cartItem");
    Preconditions.checkNotNull(cartItem.getItem().getId(), "id");

    // Create the Customer Item resource
    Representation customerItemRepresentation = publicItemRepresentation.get(cartItem.getItem());

    // Create the wrapping CartItem resource
    RepresentationFactory factory = new DefaultRepresentationFactory();

    return factory.newRepresentation("/cart/item/" + cartItem.getIndex())
      .withProperty("index", cartItem.getIndex())
      .withProperty("quantity", cartItem.getQuantity())
      .withProperty("price_subtotal", cartItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", cartItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("cart_item_subtotal", cartItem.getCartItemSubtotal().getAmount().toPlainString())
      .withRepresentation("item", customerItemRepresentation);

  }

}
