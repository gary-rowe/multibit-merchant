package org.multibit.mbm.api.response.hal.cart;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.api.response.hal.item.CustomerItemBridge;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartItem;
import org.multibit.mbm.db.dto.User;

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

  private final CustomerItemBridge customerItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerCartBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerItemBridge = new CustomerItemBridge(uriInfo, principal);
  }

  public Resource toResource(Cart cart) {

    if (cart.getId() == null) {
      throw new IllegalArgumentException("Cannot respond with a transient Cart. Id is null.");
    }

    String basePath = "/cart/" + cart.getId();

    // Create top-level resource
    Resource cartResource = getResourceFactory()
      .newResource(basePath)
      .withLink("/customer/" + cart.getCustomer().getId(), "customer")
      // TODO Implement with real account
      .withProperty("localSymbol", "&euro;")
      .withProperty("localTotal", "13.94")
      .withProperty("btcTotal", "4.78")
      // End of build
      ;

    // Create sub-resources based on items
    for (CartItem cartItem : cart.getCartItems()) {
      Resource itemResource = customerItemBridge.toResource(cartItem.getItem());
      cartResource.withSubresource("items", itemResource);
    }

    return cartResource;
  }

}
