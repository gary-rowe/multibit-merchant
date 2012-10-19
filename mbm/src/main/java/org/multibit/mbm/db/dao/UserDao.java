package org.multibit.mbm.db.dao;

import com.google.common.base.Optional;
import org.multibit.mbm.db.dto.User;

import java.util.List;

public interface UserDao {

  /**
   * Attempt to locate the User
   *
   * @param id The ID
   *
   * @return A matching User
   */
  Optional<User> getById(Long id);

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
   * @param passwordDigest The password digest
   *
   * @return A matching User with Roles and Authorities initialised
   */
  Optional<User> getByCredentials(String username, String passwordDigest);

  /**
   * Provide a paged list of all Users
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  List<User> getAllByPage(final int pageSize, final int pageNumber);

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
