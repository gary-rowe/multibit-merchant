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
 * <li>Creates representations of a CartItem for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerCartItemBridge extends BaseBridge<CartItem> {

  private final CustomerItemBridge customerItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerCartItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerItemBridge = new CustomerItemBridge(uriInfo, principal);
  }

  public Resource toResource(CartItem cartItem) {

    ResourceAsserts.assertNotNull(cartItem, "cartItem");
    ResourceAsserts.assertNotNull(cartItem.getItem().getId(),"id");

    // Create the Customer Item resource
    Resource customerItemResource = customerItemBridge.toResource(cartItem.getItem());

    // Create the wrapping CartItem resource
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/cart/item/" + cartItem.getIndex())
      .withProperty("index", cartItem.getIndex())
      .withProperty("quantity", cartItem.getQuantity())
      .withSubresource("item", customerItemResource);

  }

}
