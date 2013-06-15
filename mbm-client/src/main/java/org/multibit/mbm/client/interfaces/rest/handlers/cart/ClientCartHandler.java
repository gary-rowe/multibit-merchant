package org.multibit.mbm.client.interfaces.rest.handlers.cart;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.interfaces.rest.handlers.BaseHandler;
import org.multibit.mbm.client.interfaces.rest.api.cart.CartDto;
import org.multibit.mbm.client.interfaces.rest.api.cart.CartItemDto;
import org.multibit.mbm.client.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.client.interfaces.rest.api.cart.UpdateCartDto;
import org.multibit.mbm.client.interfaces.rest.api.user.UserDto;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of public cart requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientCartHandler extends BaseHandler {

  /**
   * @param locale The locale providing i18n information
   */
  public ClientCartHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own cart as a summary
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
   */
  public CartDto retrieveCartNoItems(UserDto clientUser) {

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

    return buildClientCartNoItems(properties);
  }

  /**
   * Retrieve the user's own cart
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
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
   * @return A matching {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
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
   * @param properties The HAL representation properties
   *
   * @return A ClientCart with all items populated
   */
  private CartDto buildClientCart(ReadableRepresentation rr, Map<String, Object> properties) {

    // Build the basic cart
    CartDto clientCart = buildClientCartNoItems(properties);

    // Read the cart items
    Collection<Map.Entry<String,ReadableRepresentation>> cartitemRepresentations = rr.getResources();

    for (Map.Entry<String, ReadableRepresentation> cartItemRepresentation : cartitemRepresentations) {

      CartItemDto cartItem = buildClientCartItem(cartItemRepresentation.getValue().getProperties());

      // Extract the embedded item
      Preconditions.checkArgument(cartItemRepresentation.getValue().getResources().size()==1);

      // TODO Fix or replace this
      //Representation itemRepresentation = cartItemRepresentation.getValue().getResources().iterator().next().getValue();
      //List<Link> links = itemRepresentation.getLinks();

      //cartItem.setItem(ClientItemHandler.buildClientItem(itemRepresentation.getProperties(), links));

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
   *
   * @param properties The HAL representation properties
   *
   * @return A ClientCart with no items populated
   */
  public static CartDto buildClientCartNoItems(Map<String, Object> properties) {

    CartDto cart = new CartDto();

    cart.setCurrencySymbol(getMandatoryPropertyAsString("currency_symbol", properties));
    cart.setCurrencyCode(getMandatoryPropertyAsString("currency_code", properties));
    cart.setPriceTotal(getMandatoryPropertyAsString("price_total", properties));
    cart.setItemTotal(getMandatoryPropertyAsString("item_total", properties));
    cart.setQuantityTotal(getMandatoryPropertyAsString("quantity_total", properties));

    return cart;
  }

}
