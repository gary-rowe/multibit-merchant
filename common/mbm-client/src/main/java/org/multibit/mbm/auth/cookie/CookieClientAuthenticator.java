package org.multibit.mbm.auth.cookie;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.multibit.mbm.auth.InMemorySessionTokenCache;
import org.multibit.mbm.model.ClientUser;

import java.util.UUID;

/**
 * <p>Authenticator to provide the following to application:</p>
 * <ul>
 * <li>Verifies the provided credentials are valid</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CookieClientAuthenticator implements Authenticator<CookieClientCredentials, ClientUser> {

  @Override
  public Optional<ClientUser> authenticate(CookieClientCredentials credentials) throws AuthenticationException {

    // Determine if the user is known by their session key
    Optional<ClientUser> user =
      InMemorySessionTokenCache.INSTANCE
        .getBySessionToken(credentials.getSessionToken());

    if (!user.isPresent()) {
      // Check if the user can be created on the fly
      if (credentials.isPublic()) {
        ClientUser publicUser = new ClientUser();
        publicUser.setSessionToken(UUID.randomUUID());
        InMemorySessionTokenCache.INSTANCE
         .put(publicUser.getSessionToken(), publicUser);
        return Optional.of(publicUser);
      }

      return Optional.absent();
    }

    // Check that their authorities match their credentials
    if (!user.get().hasAllAuthorities(credentials.getRequiredAuthorities())) {
      return Optional.absent();
    }

    // Must be OK to be here
    return user;

  }

}
