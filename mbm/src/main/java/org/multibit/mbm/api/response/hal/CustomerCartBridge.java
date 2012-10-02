package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.CustomerCartItem;
import org.multibit.mbm.api.response.CustomerCartResponse;
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
public class CustomerCartBridge extends BaseBridge<CustomerCartResponse> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerCartBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(CustomerCartResponse cartResponse) {

    String basePath = "/cart/" + cartResponse.getId();

    // TODO Instate this?
//    String slug = cartResponse
//      .getTitle()
//      .replaceAll("\\p{Punct}", "")
//      .replaceAll("\\p{Space}", "-")
//      .toLowerCase();

    Resource cartResource = getResourceFactory().newResource(basePath)
      .withProperty("btcTotal", cartResponse.getBtcTotal())
      .withProperty("localSymbol", cartResponse.getLocalSymbol())
      .withProperty("localTotal", cartResponse.getLocalTotal());


    for (CustomerCartItem cartItemResponse: cartResponse.getCartItems()) {
      cartResource.withBeanBasedSubresource("item",basePath+"/item/"+cartItemResponse.getId(),cartItemResponse);
    }

    return cartResource;
  }

}
