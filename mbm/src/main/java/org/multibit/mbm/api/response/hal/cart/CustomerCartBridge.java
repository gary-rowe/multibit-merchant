package org.multibit.mbm.api.response.hal.cart;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.api.response.hal.item.CustomerCartItemBridge;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartItem;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Customer}:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerCartBridge extends BaseBridge<Cart> {

  private final CustomerCartItemBridge customerCartItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerCartBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerCartItemBridge = new CustomerCartItemBridge(uriInfo, principal);
  }

  public Resource toResource(Cart cart) {
    ResourceAsserts.assertNotNull(cart, "cart");
    ResourceAsserts.assertNotNull(cart.getId(),"id");

    // Do not reveal the ID to non-admins
    String basePath = "/cart";

    // Create top-level resource
    Resource cartResource = getResourceFactory()
      .newResource(basePath)
      // Do not reveal the customer to non-admins
      .withLink("/customer", "customer")
      // TODO Implement with real preferences
      .withProperty("local_symbol", "&euro;")
      .withProperty("local_total", "13.94")
      .withProperty("btc_total", "4.78")
      .withProperty("item_count", String.valueOf(cart.getCartItems().size()))
      // End of build
      ;

    // Create sub-resources based on items
    for (CartItem cartItem : cart.getCartItems()) {
      Resource customerCartItemResource = customerCartItemBridge.toResource(cartItem);
      cartResource.withSubresource("cartitems", customerCartItemResource);
    }

    return cartResource;
  }

}
