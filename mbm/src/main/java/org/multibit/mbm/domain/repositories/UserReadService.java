package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.common.EntityReadService;

public interface UserReadService extends EntityReadService<User> {

  /**
   * Attempt to locate the User by a UUID
   *
   * @param apiKey The apiKey that identifies the user during HMAC authentication
   *
   * @return A matching User
   */
  Optional<User> getByApiKey(String apiKey);

  /**
   * Attempt to locate the User by a UUID
   *
   * @param username The username
   * @param passwordDigest The password digest from a one-pass digest algorithm
   *
   * @return A matching User with Roles and Authorities initialised
   */
  Optional<User> getByCredentials(String username, String passwordDigest);

  /**
   * Persist the given User
   *
   * @param User A User (either new or updated)
   *
   * @return The persisted User
   */
  User saveOrUpdate(User User);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
