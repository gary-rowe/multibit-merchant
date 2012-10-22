package org.multibit.mbm.client.handlers.cart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.model.ClientCart;
import org.multibit.mbm.model.ClientUser;

import java.io.Reader;
import java.io.StringReader;
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
public class CustomerCartHandler extends BaseHandler {

  /**
   * @param locale       The locale providing i18n information
   */
  public CustomerCartHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own cart
   *
   * @param clientUser The authenticated client user
   *
   * @return A matching {@link org.multibit.mbm.model.PublicItem}
   */
  public ClientCart retrieveCart(ClientUser clientUser) {

    // Sanity check
    Preconditions.checkNotNull(clientUser);
    Preconditions.checkNotNull(clientUser.getApiKey());
    Preconditions.checkNotNull(clientUser.getSecretKey());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/cart");

    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .get(String.class);

    // Read the HAL
    ReadableResource rr = readHalRepresentation(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();

    ClientCart clientCart = new ClientCart();
    String localSymbol = (String) properties.get("local_symbol").get();
    String localTotal = (String) properties.get("local_total").get();
    String btcTotal = (String) properties.get("btc_total").get();

    clientCart.setLocalSymbol(localSymbol);
    clientCart.setLocalTotal(localTotal);
    clientCart.setBtcTotal(btcTotal);

    return clientCart;
  }

  private ReadableResource readHalRepresentation(String hal) {
    ResourceFactory rf = getResourceFactory();
    Reader reader = new StringReader(hal);
    return rf.readResource(reader);
  }
}
