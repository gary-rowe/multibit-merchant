package org.multibit.mbm.client.interfaces.rest.handlers.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.interfaces.rest.handlers.BaseHandler;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.interfaces.rest.api.user.UserDto;
import org.multibit.mbm.client.interfaces.rest.api.user.WebFormRegistrationDto;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.multibit.mbm.client.interfaces.rest.auth.webform.WebFormClientRegistration;

import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of client user registration requests (anonymous and web form)</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicUserHandler extends BaseHandler {

  /**
   * @param locale       The locale providing i18n information
   */
  public PublicUserHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve the user's own profile
   *
   * @param registration The web form registration details provided by the user
   *
   * @return A matching user
   */
  public Optional<UserDto> registerWithWebForm(WebFormClientRegistration registration) {

    // Sanity check
    Preconditions.checkNotNull(registration);

    WebFormRegistrationDto entity = new WebFormRegistrationDto();
    entity.setUsername(registration.getUsername());
    entity.setPasswordDigest(registration.getPasswordDigest());

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/client/user/register");

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

    // Must assume that the registration was successful
    // Using the credentials later would mean failed authentication anyway
    clientUser.setApiKey(apiKey);
    clientUser.setSecretKey(secretKey);
    clientUser.setCachedAuthorities(new Authority[] {Authority.ROLE_CUSTOMER});

    return Optional.of(clientUser);
  }

  /**
   * Register an anonymous user for the current session
   *
   * @return A matching user
   */
  public Optional<UserDto> registerAnonymously() {

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/client/user/anonymous");

    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
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

    // Must assume that the registration was successful
    // Using the credentials later would mean failed authentication anyway
    clientUser.setApiKey(apiKey);
    clientUser.setSecretKey(secretKey);
    clientUser.setCachedAuthorities(new Authority[]{Authority.ROLE_PUBLIC});

    return Optional.of(clientUser);
  }


}
