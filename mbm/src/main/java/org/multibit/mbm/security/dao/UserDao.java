package org.multibit.mbm.security.dao;

import org.multibit.mbm.security.dto.User;

public interface UserDao {

  /**
   * Attempt to locate the User
   * @param openId The OpenId token
   * @return A matching User
   * @throws UserNotFoundException If something goes wrong
   */
  User getUserByOpenId(String openId) throws UserNotFoundException;

  /**
   * Attempt to locate the User by a UUID
   * @param uuid The UUID that acts as a unique identifier when Open ID is not available
   * @return A matching User
   */
  User getUserByUUID(String uuid);

  /**
   * Persist the given User
   * @param User A User (either new or updated)
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
