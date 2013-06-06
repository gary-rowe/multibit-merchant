package org.multibit.mbm.client.handlers.delivery;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.spi.Link;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.request.cart.PublicCartItem;
import org.multibit.mbm.interfaces.rest.api.request.cart.PublicUpdateCartRequest;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.client.handlers.item.ClientItemHandler;
import org.multibit.mbm.model.ClientCart;
import org.multibit.mbm.model.ClientCartItem;
import org.multibit.mbm.model.ClientUser;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.SupplierMerchantClient}:</p>
 * <ul>
 * <li>Construction of supplier delivery requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class SupplierDeliveryHandler extends BaseHandler {

  /**
   * @param locale The locale providing i18n information
   */
  public SupplierDeliveryHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own cart
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.model.ClientItem}
   */
  public ClientCart retrieveCart(ClientUser clientUser) {

    // Sanity check
    Preconditions.checkNotNull(clientUser);
    Preconditions.checkNotNull(clientUser.getApiKey());
    Preconditions.checkNotNull(clientUser.getSecretKey());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/cart");

    String hal = HalHmacResourceFactory.INSTANCE
      .newUserResource(locale, path, clientUser)
      .get(String.class);

    // Read the HAL
    ReadableResource rr = unmarshalHal(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();

    return buildClientCart(rr, properties);
  }

  /**
   * Update the cart items
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.model.ClientItem}
   */
  public ClientCart updateCartItems(ClientUser clientUser, List<PublicCartItem> cartItems) {

    // Sanity check
    Preconditions.checkNotNull(clientUser);
    Preconditions.checkNotNull(clientUser.getApiKey());
    Preconditions.checkNotNull(clientUser.getSecretKey());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/cart");

    PublicUpdateCartRequest updateCartRequest = new PublicUpdateCartRequest();
    updateCartRequest.setCartItems(cartItems);

    String hal = HalHmacResourceFactory.INSTANCE
      .newUserResource(locale, path, clientUser)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    // Read the HAL
    ReadableResource rr = unmarshalHal(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();

    return buildClientCart(rr, properties);
  }

  /**
   * @param properties The HAL resource properties
   *
   * @return A ClientCart with all items populated
   */
  private ClientCart buildClientCart(ReadableResource rr, Map<String, Optional<Object>> properties) {

    // Build the basic cart
    ClientCart clientCart = buildClientCartNoItems(properties);

    // Read the cart items
    List<Resource> cartitemResources = rr.getResources();

    for (Resource cartItemResource : cartitemResources) {

      ClientCartItem cartItem = buildClientCartItem(cartItemResource.getProperties());

      // Extract the embedded item
      Preconditions.checkArgument(cartItemResource.getResources().size()==1);

      Resource itemResource = cartItemResource.getResources().get(0);
      List<Link> links = itemResource.getLinks();

      cartItem.setItem(ClientItemHandler.buildClientItem(itemResource.getProperties(), links));

      clientCart.getCartItems().add(cartItem);

    }

    return clientCart;
  }

  private ClientCartItem buildClientCartItem(Map<String, Optional<Object>> properties) {

    ClientCartItem cartItem = new ClientCartItem();
    Integer index = getMandatoryPropertyAsInteger("index",properties);
    Integer quantity = getMandatoryPropertyAsInteger("quantity", properties);
    String priceSubtotal = getMandatoryPropertyAsString("price_subtotal", properties);
    String taxSubtotal = getMandatoryPropertyAsString("tax_subtotal", properties);
    String cartItemSubtotal = getMandatoryPropertyAsString("cart_item_subtotal", properties);

    cartItem.setIndex(index);
    cartItem.setQuantity(quantity);
    cartItem.setPriceSubtotal(priceSubtotal);
    cartItem.setTaxSubtotal(taxSubtotal);
    cartItem.setCartItemSubtotal(cartItemSubtotal);

    return cartItem;
  }

  /**
   * @param properties The HAL resource properties
   *
   * @return A ClientCart with no items populated
   */
  public static ClientCart buildClientCartNoItems(Map<String, Optional<Object>> properties) {

    ClientCart clientCart = new ClientCart();

    clientCart.setCurrencySymbol(getMandatoryPropertyAsString("currency_symbol", properties));
    clientCart.setCurrencyCode(getMandatoryPropertyAsString("currency_code", properties));
    clientCart.setPriceTotal(getMandatoryPropertyAsString("price_total", properties));
    clientCart.setItemTotal(getMandatoryPropertyAsString("item_total", properties));
    clientCart.setQuantityTotal(getMandatoryPropertyAsString("quantity_total", properties));

    return clientCart;
  }

}
