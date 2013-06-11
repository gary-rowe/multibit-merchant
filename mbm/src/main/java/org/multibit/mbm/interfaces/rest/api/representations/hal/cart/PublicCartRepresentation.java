package org.multibit.mbm.interfaces.rest.api.representations.hal.cart;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.CartItem;
import org.multibit.mbm.interfaces.rest.api.representations.hal.item.PublicCartItemRepresentation;

/**
 * <p>Representation to provide the following to the anonymous public and Customers:</p>
 * <ul>
 * <li>Creates a {@link Representation} of a shopping cart</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicCartRepresentation {

  private final PublicCartItemRepresentation publicCartItemRepresentation = new PublicCartItemRepresentation();

  public Representation get(Cart cart) {
    Preconditions.checkNotNull(cart, "cart");
    Preconditions.checkNotNull(cart.getId(), "id");

    // Do not reveal the ID to non-admins
    String basePath = "/cart";

    // TODO Integrate with Preferences and CartItem
    String currencySymbol = "Ƀ"; // or &pound; or &euro;
    String currencyCode = "BTC";

    // Calculate the value of the cart items
    // TODO Allow for currency conversion
    BigMoney cartTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    BigMoney taxTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    for (CartItem cartItem : cart.getCartItems()) {
      cartTotal = cartTotal.plus(cartItem.getPriceSubtotal());
      taxTotal = taxTotal.plus(cartItem.getTaxSubtotal());
    }

    // Create top-level resource
    Representation cartRepresentation= new DefaultRepresentationFactory()
      .newRepresentation(basePath)
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
      Representation publicCartItemRepresentation= new PublicCartItemRepresentation().get(cartItem);
      cartRepresentation.withRepresentation("cartitems", publicCartItemRepresentation);
    }

    return cartRepresentation;
  }

}
