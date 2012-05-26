package org.multibit.mbm.auth.hmac;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.persistence.dto.User;

/**
 * <p>Authenticator to provide the following to application:</p>
 * <ul>
 * <li>Verifies the provided credentials are valid</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class HmacAuthenticator implements Authenticator<HmacCredentials, User> {
  @Override
  public Optional<User> authenticate(HmacCredentials credentials) throws AuthenticationException {
    // TODO Link this into the security system via the apiKey (user name? openId?)
    return null;
  }
}
