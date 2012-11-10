package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.CartItem;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Item}:</p>
 * <ul>
 * <li>Creates representations of a CartItem for the anonymous public</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicCartItemBridge extends BaseBridge<CartItem> {

  private final PublicItemBridge publicItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public PublicCartItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    publicItemBridge = new PublicItemBridge(uriInfo, principal);
  }

  public Resource toResource(CartItem cartItem) {

    ResourceAsserts.assertNotNull(cartItem, "cartItem");
    ResourceAsserts.assertNotNull(cartItem.getItem().getId(),"id");

    // Create the Customer Item resource
    Resource customerItemResource = publicItemBridge.toResource(cartItem.getItem());

    // Create the wrapping CartItem resource
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/cart/item/" + cartItem.getIndex())
      .withProperty("index", cartItem.getIndex())
      .withProperty("quantity", cartItem.getQuantity())
      .withProperty("price_subtotal", cartItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", cartItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("cart_item_subtotal", cartItem.getCartItemSubtotal().getAmount().toPlainString())
      .withSubresource("item", customerItemResource);

  }

}
