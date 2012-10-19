package org.multibit.mbm.client.handlers.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import org.multibit.mbm.auth.webform.WebFormClientCredentials;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.model.ClientUser;
import org.multibit.mbm.model.CustomerUser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of public single item requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CustomerUserHandler extends BaseHandler {

  /**
   * @param locale       The locale providing i18n information
   */
  public CustomerUserHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own profile
   *
   * @param credentials The web form credentials provided by the user
   *
   * @return A matching {@link org.multibit.mbm.model.PublicItem}
   */
  public Optional<ClientUser> authenticateWithWebForm(WebFormClientCredentials credentials) {

    // Sanity check

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/mbm/users/authenticate");

    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .entity(credentials)
      .post(String.class);

    // Read the HAL
    ReadableResource rr = readHalRepresentation(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();


    ClientUser clientUser = new ClientUser();
    // Mandatory properties (will cause IllegalStateException if not present)
    clientUser.setApiKey((String) properties.get("apiKey").get());
    clientUser.setSecretKey((String) properties.get("secretKey").get());
    clientUser.setUsername((String) properties.get("username").get());
    clientUser.setPasswordDigest((String) properties.get("password").get());

    return Optional.of(clientUser);
  }

  /**
   * Retrieve the user's own profile
   *
   * @param clientUser The ClientUser containing the API access information
   *
   * @return A matching {@link org.multibit.mbm.model.PublicItem}
   */
  public Optional<CustomerUser> getOwnProfile(ClientUser clientUser) {

    // Sanity check

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/mbm/users");
    String hal = HalHmacResourceFactory.INSTANCE
      .newUserResource(locale, path, clientUser)
      .get(String.class);

    // Read the HAL
    ReadableResource rr = readHalRepresentation(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();

    CustomerUser customerUser = new CustomerUser();
    // Mandatory properties (will cause IllegalStateException if not present)
    customerUser.setSKU((String) properties.get("sku").get());
    // Optional direct properties
    if (properties.containsKey("gtin")) {
      Optional<Object> gtin = properties.get("gtin");
      if (gtin.isPresent()) {
        customerUser.setGTIN((String) gtin.get());
      }
    }
    // Optional properties
    for (Map.Entry<String,Optional<Object>> entry: properties.entrySet()) {
      customerUser.getOptionalProperties().put(entry.getKey(), (String) entry.getValue().get());
    }

    return Optional.of(customerUser);
  }

  private ReadableResource readHalRepresentation(String hal) {
    ResourceFactory rf = getResourceFactory();
    Reader reader = new StringReader(hal);
    return rf.readResource(reader);
  }
}
