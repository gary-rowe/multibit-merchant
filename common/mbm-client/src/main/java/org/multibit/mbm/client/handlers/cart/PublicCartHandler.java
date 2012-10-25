package org.multibit.mbm.client.handlers.cart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.spi.Link;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.client.handlers.item.ClientItemHandler;
import org.multibit.mbm.model.ClientCart;
import org.multibit.mbm.model.ClientCartItem;
import org.multibit.mbm.model.ClientUser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of customer cart requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicCartHandler extends BaseHandler {

  /**
   * @param locale The locale providing i18n information
   */
  public PublicCartHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own cart as a summary
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.model.ClientItem}
   */
  public ClientCart retrieveCartNoItems(ClientUser clientUser) {

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

    return buildClientCartNoItems(properties);
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

    cartItem.setIndex(index);
    cartItem.setQuantity(quantity);

    return cartItem;
  }

  /**
   * @param properties The HAL resource properties
   *
   * @return A ClientCart with no items populated
   */
  public static ClientCart buildClientCartNoItems(Map<String, Optional<Object>> properties) {

    ClientCart clientCart = new ClientCart();

    clientCart.setLocalSymbol(getMandatoryPropertyAsString("local_symbol",properties));
    clientCart.setLocalTotal(getMandatoryPropertyAsString("local_total",properties));
    clientCart.setBtcTotal(getMandatoryPropertyAsString("btc_total",properties));
    clientCart.setItemCount(getMandatoryPropertyAsString("item_count",properties));

    return clientCart;
  }

}
