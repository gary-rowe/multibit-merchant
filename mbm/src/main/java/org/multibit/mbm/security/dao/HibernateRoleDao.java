package org.multibit.mbm.security.dao;

import org.multibit.mbm.security.dto.Authority;
import org.multibit.mbm.security.dto.Role;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("hibernateRoleDao")
public class HibernateRoleDao implements RoleDao {

  @Resource(name = "hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Role getRoleByAuthority(Authority authority) throws RoleNotFoundException {
    return getRoleByName(authority.name());
  }

  @Override
  public Role getRoleByName(String name) throws RoleNotFoundException {
    List roles = hibernateTemplate.find("from Role r where r.name = ?", name);
    if (roles==null || roles.isEmpty()) {
      throw new RoleNotFoundException();
    }
    return (Role) roles.get(0);
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


  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
