package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.repositories.common.EntityReadService;
import org.multibit.mbm.interfaces.rest.auth.Authority;

public interface RoleReadService extends EntityReadService<Role> {

  /**
   * Attempt to locate the Role using a shortcut Authority
   *
   * @param authority The Role shortcut Authority  (e.g. "ROLE_CUSTOMER")
   * @return A matching Role
   */
  Optional<Role> getByAuthority(Authority authority);

  /**
   * Attempt to locate the Role using a name
   *
   * @param name The Role name (e.g. "ROLE_SPECIAL") which has been defined by an admin
   * @return A matching Role
   */
  Optional<Role> getByName(String name);

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
