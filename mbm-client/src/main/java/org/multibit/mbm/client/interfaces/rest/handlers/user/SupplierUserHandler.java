package org.multibit.mbm.client.interfaces.rest.handlers.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.interfaces.rest.handlers.BaseHandler;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.interfaces.rest.api.user.CustomerUserDto;
import org.multibit.mbm.client.interfaces.rest.api.user.UserDto;
import org.multibit.mbm.client.interfaces.rest.api.user.WebFormAuthenticationDto;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.multibit.mbm.client.interfaces.rest.auth.webform.WebFormClientCredentials;

import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.SupplierMerchantClient}:</p>
 * <ul>
 * <li>Construction of supplier user requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class SupplierUserHandler extends BaseHandler {

  /**
   * @param locale       The locale providing i18n information
   */
  public SupplierUserHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own profile
   *
   * @param credentials The web form credentials provided by the user
   *
   * @return A matching {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
   */
  public Optional<UserDto> authenticateWithWebForm(WebFormClientCredentials credentials) {

    // Sanity check
    Preconditions.checkNotNull(credentials);
    Preconditions.checkNotNull(credentials.getUsername());
    Preconditions.checkNotNull(credentials.getPasswordDigest());

    WebFormAuthenticationDto entity = new WebFormAuthenticationDto(
      credentials.getUsername(),
      credentials.getPasswordDigest()
    );

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/client/user/authenticate");

    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .entity(entity, HalMediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    // Read the HAL
    ReadableRepresentation rr = unmarshalHal(hal);

    Map<String, Object> properties = rr.getProperties();

    UserDto clientUser = new UserDto();
    String apiKey = (String) properties.get("api_key");
    String secretKey = (String) properties.get("secret_key");

    if ("".equals(apiKey) || "".equals(secretKey)) {
      return Optional.absent();
    }

    // Must assume that the authentication was successful
    // Using the credentials later would mean failed authentication anyway
    clientUser.setApiKey(apiKey);
    clientUser.setSecretKey(secretKey);
    clientUser.setCachedAuthorities(new Authority[] {Authority.ROLE_CUSTOMER});

    return Optional.of(clientUser);
  }

  /**
   * Retrieve the user's own profile
   *
   * @param clientUser The ClientUser containing the API access information
   *
   * @return A matching {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
   */
  public Optional<CustomerUserDto> retrieveProfile(UserDto clientUser) {

    // Sanity check
    Preconditions.checkNotNull(clientUser);
    Preconditions.checkNotNull(clientUser.getApiKey());
    Preconditions.checkNotNull(clientUser.getSecretKey());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/users");
    String hal = HalHmacResourceFactory.INSTANCE
      .newUserResource(locale, path, clientUser)
      .get(String.class);

    // Read the HAL
    ReadableRepresentation rr = unmarshalHal(hal);

    Map<String, Object> properties = rr.getProperties();

    CustomerUserDto customerUser = new CustomerUserDto();
    // Mandatory properties (will cause IllegalStateException if not present)
    // Optional direct properties
    // Optional properties
    for (Map.Entry<String, Object> entry : properties.entrySet()) {
      customerUser.getOptionalProperties().put(entry.getKey(), (String) entry.getValue());
    }

    return Optional.of(customerUser);
  }

}
