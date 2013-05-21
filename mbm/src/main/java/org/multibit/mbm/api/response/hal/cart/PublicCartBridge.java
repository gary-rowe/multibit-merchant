package org.multibit.mbm.api.response.hal.cart;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.api.response.hal.item.PublicCartItemBridge;
import org.multibit.mbm.core.model.Cart;
import org.multibit.mbm.core.model.CartItem;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to the anonymous public and Customers:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations of a shopping cart</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicCartBridge extends BaseBridge<Cart> {

  private final PublicCartItemBridge publicCartItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.core.model.User} to provide a security principal
   */
  public PublicCartBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    publicCartItemBridge = new PublicCartItemBridge(uriInfo, principal);
  }

  public Resource toResource(Cart cart) {
    ResourceAsserts.assertNotNull(cart, "cart");
    ResourceAsserts.assertNotNull(cart.getId(),"id");

    // Do not reveal the ID to non-admins
    String basePath = "/cart";

    // TODO Integrate with Preferences and CartItem
    String currencySymbol = "Ƀ"; // or &pound; or &euro;
    String currencyCode = "BTC";

    // Calculate the value of the cart items
    // TODO Allow for currency conversion
    BigMoney cartTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    BigMoney taxTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    for (CartItem cartItem: cart.getCartItems()) {
      cartTotal = cartTotal.plus(cartItem.getPriceSubtotal());
      taxTotal = taxTotal.plus(cartItem.getTaxSubtotal());
    }

    // Create top-level resource
    Resource cartResource = getResourceFactory()
      .newResource(basePath)
      // Do not reveal the customer to non-admins
      .withLink("/customer", "customer")
      .withProperty("currency_symbol", currencySymbol)
      .withProperty("currency_code", currencyCode)
      .withProperty("price_total", cartTotal.getAmount().toPlainString())
      .withProperty("tax_total", taxTotal.getAmount().toPlainString())
      .withProperty("item_total", cart.getItemTotal())
      .withProperty("quantity_total", cart.getQuantityTotal())
      // End of build
      ;

    // Create sub-resources based on items
    for (CartItem cartItem : cart.getCartItems()) {
      Resource publicCartItemResource = publicCartItemBridge.toResource(cartItem);
      cartResource.withSubresource("cartitems", publicCartItemResource);
    }

    return cartResource;
  }

}
