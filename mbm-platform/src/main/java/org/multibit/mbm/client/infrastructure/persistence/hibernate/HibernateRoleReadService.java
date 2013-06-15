package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.Role;
import org.multibit.mbm.client.domain.repositories.RoleReadService;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateRoleDao")
public class HibernateRoleReadService extends BaseHibernateReadService<Role> implements RoleReadService {

  @SuppressWarnings("unchecked")
  @Override
  public Optional<Role> getById(Long id) {
    return getById(Role.class, id);
  }

  @Override
  public Optional<Role> getByAuthority(Authority authority) {
    return getByName(authority.name());
  }

  @Override
  public Optional<Role> getByName(String name) {
    List roles = hibernateTemplate.find("from Role r where r.name = ?", name);
    return first(roles);
  }

  public PaginatedList<Role> getPaginatedList(final int pageSize, final int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, Role.class);
  }

  @Override
  public Role saveOrUpdate(Role role) {
    hibernateTemplate.saveOrUpdate(role);
    return role;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }

  @Override
  protected Role initialized(Role entity) {
    return entity;
  }

  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
