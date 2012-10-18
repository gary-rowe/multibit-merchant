package org.multibit.mbm.db.dao;

import com.google.common.base.Optional;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Role;

import java.util.List;

public interface RoleDao {

  /**
   * Attempt to locate the Role
   *
   * @param id The ID
   *
   * @return A matching Role
   */
  Optional<Role> getById(Long id);

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
   * Provide a paged list of all Roles
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  List<Role> getAllByPage(final int pageSize, final int pageNumber);

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
