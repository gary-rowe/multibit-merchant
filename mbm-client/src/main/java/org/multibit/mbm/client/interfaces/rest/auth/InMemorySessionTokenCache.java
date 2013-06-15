package org.multibit.mbm.client.interfaces.rest.auth;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.multibit.mbm.client.interfaces.rest.api.user.UserDto;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>Cache to provide the following to {@link org.multibit.mbm.client.interfaces.rest.api.user.UserDto} authenticators:</p>
 * <ul>
 * <li>In-memory thread-safe cache for client user instances</li>
 * <li>Provision of fast lookup for request authentication</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public enum InMemorySessionTokenCache {

  // Provide a global singleton for the application
  INSTANCE;

  // A lot of threads will hit this cache
  private volatile Cache<UUID, UserDto> sessionTokenCache;

  InMemorySessionTokenCache() {
    reset(15,TimeUnit.MINUTES);
  }

  /**
   * Resets the cache and allows the expiry time to be set (perhaps for testing)
   *
   * @param duration The duration before a user must manually authenticate through a web form due to inactivity
   * @param unit     The {@link TimeUnit} that duration is expressed in
   */
  public InMemorySessionTokenCache reset(int duration, TimeUnit unit) {

    // Build the cache
    if (sessionTokenCache != null) {
      sessionTokenCache.invalidateAll();
    }

    // If there is no activity against a key then we want
    // it to be expired from the cache, but each fresh write
    // will reset the expiry timer
    sessionTokenCache = CacheBuilder
      .newBuilder()
      .expireAfterWrite(duration, unit)
      .maximumSize(1000)
      .build();

    return INSTANCE;
  }

  /**
   * @param sessionToken The session token to locate the user
   *
   * @return The matching ClientUser or absent
   */
  public Optional<UserDto> getBySessionToken(Optional<UUID> sessionToken) {

    // Fail fast
    if (!sessionToken.isPresent()) {
      return Optional.absent();
    }

    // Check the cache
    Optional<UserDto> clientUser = Optional.fromNullable(sessionTokenCache.getIfPresent(sessionToken.get()));

    // TODO What is a better way of implementing this?
    if (clientUser.isPresent()) {
      // Ensure we refresh the cache on a check to maintain the session timeout
      sessionTokenCache.put(clientUser.get().getSessionToken(), clientUser.get());
    }

    return clientUser;

  }

  /**
   * @param sessionToken The session token to use to locate the user
   * @param clientUser   The client user to cache
   */
  public void put(UUID sessionToken, UserDto clientUser) {
    Preconditions.checkNotNull(clientUser);
    sessionTokenCache.put(sessionToken, clientUser);
  }

}
