package org.multibit.mbm.auth.webform;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.client.CustomerMerchantClient;
import org.multibit.mbm.model.ClientUser;

import java.util.Locale;

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

    // Determine if the upstream server can authenticate
    Optional<ClientUser> user = CustomerMerchantClient
      .newInstance(Locale.getDefault())
      .user()
      .authenticateWithWebForm(credentials);

    if (!user.isPresent()) {
      return Optional.absent();
    }

    // Must be OK to be here
    return user;

  }

}
