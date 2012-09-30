package org.multibit.mbm.db.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dao.UserNotFoundException;
import org.multibit.mbm.db.dto.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Repository("hibernateUserDao")
public class HibernateUserDao implements UserDao {

  @Resource(name = "hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public User getUserByOpenId(String openId) throws UserNotFoundException {
    List users = hibernateTemplate.find("from User u where u.openId = ?", openId);
    if (users == null || users.isEmpty()) {
      throw new UserNotFoundException();
    }
    return (User) users.get(0);
  }

  @Override
  public User getUserByUUID(String uuid) {
    List users = hibernateTemplate.find("from User u where u.uuid = ?", uuid);
    if (users == null || users.isEmpty()) {
      throw new UserNotFoundException();
    }
    return (User) users.get(0);
  }

  @Override
  @SuppressWarnings("unchecked")
  public User getUserByCredentials(String username, String password) {
    List<User> users = hibernateTemplate.find("from User u where u.username = ? ", username);
    if (users == null || users.isEmpty()) {
      throw new UserNotFoundException();
    }
    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    // Check the password against all matching Users
    for (User user : users) {
      if (passwordEncryptor.checkPassword(password, user.getPassword())) {
        return user;
      }
    }

    // Must have failed to be here
    throw new UserNotFoundException();
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
