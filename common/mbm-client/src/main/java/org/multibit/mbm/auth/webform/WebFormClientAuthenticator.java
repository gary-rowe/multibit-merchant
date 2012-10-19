package org.multibit.mbm.auth.webform;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.auth.InMemorySessionTokenCache;
import org.multibit.mbm.client.CustomerMerchantClient;
import org.multibit.mbm.model.ClientUser;

import java.util.Locale;
import java.util.UUID;

/**
 * <p>Authenticator to provide the following to application:</p>
 * <ul>
 * <li>Verifies the provided credentials are valid</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class WebFormClientAuthenticator implements Authenticator<WebFormClientCredentials, ClientUser> {

  @Override
  public Optional<ClientUser> authenticate(WebFormClientCredentials credentials) throws AuthenticationException {

    Optional<ClientUser> clientUserOptional = Optional.absent();

    try {
      // Determine if the upstream server can authenticate
      // We do not trawl the cookie cache since this is a user refresh
      clientUserOptional = CustomerMerchantClient
        .newInstance(Locale.getDefault())
        .user()
        .authenticateWithWebForm(credentials);

      if (!clientUserOptional.isPresent()) {
        return Optional.absent();
      }

      // User has been authenticated by the upstream server

      // Create a session token to allow ongoing cookie authentication
      UUID sessionToken = UUID.randomUUID();

      ClientUser clientUser = clientUserOptional.get();
      clientUser.setSessionToken(sessionToken);

      // Cache this user to allow cookie authentication
      InMemorySessionTokenCache
        .INSTANCE
        .put(sessionToken, clientUserOptional.get());

    } catch (IllegalArgumentException e) {
      throw new AuthenticationException("Illegal argument in web form authenticator", e);
    } catch (NullPointerException e) {
      throw new AuthenticationException("Mandatory fields missing in web form authenticator", e);
    } catch (IllegalStateException e) {
      throw new AuthenticationException("Illegal state in web form authenticator", e);
    }

    return clientUserOptional;

  }

}
