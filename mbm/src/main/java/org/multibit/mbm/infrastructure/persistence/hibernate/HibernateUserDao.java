package org.multibit.mbm.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.multibit.mbm.domain.repositories.UserDao;
import org.multibit.mbm.domain.model.model.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernateUserDao")
public class HibernateUserDao extends BaseHibernateDao<User> implements UserDao {

  @SuppressWarnings("unchecked")
  @Override
  public Optional<User> getById(Long id) {
    return getById(User.class, id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Optional<User> getByApiKey(String uuid) {
    List users = hibernateTemplate.find("from User u where u.apiKey = ?", uuid);
    return first(users);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<User> getByCredentials(String username, String passwordDigest) {
    List<User> users = hibernateTemplate.find("from User u where u.username = ? ", username);

    if (isNotFound(users)) return Optional.absent();

    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    // Check the password against all matching Users
    for (User user : users) {
      if (passwordEncryptor.checkPassword(passwordDigest, user.getPasswordDigest())) {
        return Optional.of(initialized(user));
      }
    }

    // Must have failed to be here
    return Optional.absent();
  }

  /**
   * Initialize various collections since we are targeting the individual entity (perhaps for display)
   *
   * @param entity The entity
   *
   * @return The entity with all collections initialized
   */
  @Override
  protected User initialized(User entity) {
    hibernateTemplate.initialize(entity.getContactMethodMap());
    if (entity.getCustomer() != null) {
      hibernateTemplate.initialize(entity.getCustomer());
    }
    if (entity.getSupplier() != null) {
      hibernateTemplate.initialize(entity.getSupplier());
    }
    return entity;
  }

  @SuppressWarnings("unchecked")
  public List<User> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<User>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from User");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
  }

  @Override
  public User saveOrUpdate(User user) {
    hibernateTemplate.saveOrUpdate(user);
    return user;
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
