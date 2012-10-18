package org.multibit.mbm.auth;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.multibit.mbm.model.ClientUser;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>Cache to provide the following to {@link org.multibit.mbm.model.ClientUser} authenticators:</p>
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

  private final Cache<UUID, ClientUser> sessionTokenCache;

  InMemorySessionTokenCache() {

    // Build the cache
    sessionTokenCache = CacheBuilder
      .newBuilder()
      .expireAfterWrite(10, TimeUnit.MINUTES)
      .maximumSize(1000)
      .build();
  }

  /**
   * @param sessionToken The session token to locate the user
   * @return The matching ClientUser or absent
   */
  public Optional<ClientUser> getBySessionToken(UUID sessionToken) {
    return Optional.fromNullable(sessionTokenCache.getIfPresent(sessionToken));
  }

  /**
   * @param sessionToken The session token to use to locate the user
   * @param clientUser The client user to cache
   */
  public void put(UUID sessionToken, ClientUser clientUser) {
    Preconditions.checkNotNull(clientUser);
    sessionTokenCache.put(sessionToken, clientUser);
  }

}
