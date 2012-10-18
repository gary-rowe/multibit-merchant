package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Role;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernateRoleDao")
public class HibernateRoleDao extends BaseHibernateDao implements RoleDao {

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
    return first(roles, Role.class);
  }

  @SuppressWarnings("unchecked")
  public List<Role> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<Role>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from Role");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
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
