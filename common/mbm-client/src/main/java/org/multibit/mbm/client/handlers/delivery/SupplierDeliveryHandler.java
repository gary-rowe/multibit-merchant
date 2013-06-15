package org.multibit.mbm.client.handlers.delivery;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.interfaces.rest.api.cart.CartDto;
import org.multibit.mbm.interfaces.rest.api.cart.CartItemDto;
import org.multibit.mbm.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.interfaces.rest.api.cart.UpdateCartDto;
import org.multibit.mbm.interfaces.rest.api.user.UserDto;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
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
   * @return A matching {@link org.multibit.mbm.interfaces.rest.api.item.ItemDto}
   */
  public CartDto retrieveCart(UserDto clientUser) {

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
    ReadableRepresentation rr = unmarshalHal(hal);

    Map<String, Object> properties = rr.getProperties();

    return buildClientCart(rr, properties);
  }

  /**
   * Update the cart items
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.interfaces.rest.api.item.ItemDto}
   */
  public CartDto updateCartItems(UserDto clientUser, List<PublicCartItemDto> cartItems) {

    // Sanity check
    Preconditions.checkNotNull(clientUser);
    Preconditions.checkNotNull(clientUser.getApiKey());
    Preconditions.checkNotNull(clientUser.getSecretKey());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/cart");

    UpdateCartDto updateCartRequest = new UpdateCartDto();
    updateCartRequest.setCartItems(cartItems);

    String hal = HalHmacResourceFactory.INSTANCE
      .newUserResource(locale, path, clientUser)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    // Read the HAL
    ReadableRepresentation rr = unmarshalHal(hal);

    Map<String, Object> properties = rr.getProperties();

    return buildClientCart(rr, properties);
  }

  /**
   * @param properties The HAL resource properties
   *
   * @return A ClientCart with all items populated
   */
  private CartDto buildClientCart(ReadableRepresentation rr, Map<String, Object> properties) {

    // Build the basic cart
    CartDto clientCart = buildClientCartNoItems(properties);

    // Read the cart items
    Collection<Map.Entry<String, ReadableRepresentation>> cartitemResources = rr.getResources();

    for (Map.Entry<String, ReadableRepresentation> cartItemResource : cartitemResources) {

      CartItemDto cartItem = buildClientCartItem(cartItemResource.getValue().getProperties());

      // Extract the embedded item
      Preconditions.checkArgument(cartItemResource.getValue().getResources().size()==1);

      // TODO Fix or replace this
      //Representation itemResource = cartItemResource.getValue().getResources().iterator().next().getValue();
      //List<Link> links = itemResource.getLinks();

      //cartItem.setItem(ClientItemHandler.buildClientItem(itemResource.getProperties(), links));

      clientCart.getCartItems().add(cartItem);

    }

    return clientCart;
  }

  private CartItemDto buildClientCartItem(Map<String, Object> properties) {

    CartItemDto cartItem = new CartItemDto();
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
  public static CartDto buildClientCartNoItems(Map<String, Object> properties) {

    CartDto clientCart = new CartDto();

    clientCart.setCurrencySymbol(getMandatoryPropertyAsString("currency_symbol", properties));
    clientCart.setCurrencyCode(getMandatoryPropertyAsString("currency_code", properties));
    clientCart.setPriceTotal(getMandatoryPropertyAsString("price_total", properties));
    clientCart.setItemTotal(getMandatoryPropertyAsString("item_total", properties));
    clientCart.setQuantityTotal(getMandatoryPropertyAsString("quantity_total", properties));

    return clientCart;
  }

}
