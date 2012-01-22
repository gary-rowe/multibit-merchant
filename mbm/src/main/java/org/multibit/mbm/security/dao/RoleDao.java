package org.multibit.mbm.security.dao;

import org.multibit.mbm.security.dto.Role;

public interface RoleDao {

  /**
   * Attempt to locate the Role
   * @param name The name (e.g. "ROLE_ADMIN")
   * @return A matching Role
   * @throws org.multibit.mbm.security.dao.RoleNotFoundException If something goes wrong
   */
  Role getRoleByName(String name) throws RoleNotFoundException;

  /**
   * Persist the given Role
   * @param Role A Role (either new or updated)
   * @return The persisted Role
   */
  Role saveOrUpdate(Role Role);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
