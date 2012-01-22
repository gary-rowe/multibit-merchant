package org.multibit.mbm.security.dao;

import org.multibit.mbm.security.dto.User;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("hibernateUserDao")
public class HibernateUserDao implements UserDao {

  @Resource(name="hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public User getUserByOpenId(String openId) throws UserNotFoundException {
    List users = hibernateTemplate.find("from User u where u.openId = ?", openId);
    if (users==null || users.isEmpty()) {
      throw new UserNotFoundException();
    }
    return (User) users.get(0);
  }

  @Override
  public User getUserByUUID(String uuid) {
    List users = hibernateTemplate.find("from User u where u.uuid = ?", uuid);
    if (users==null || users.isEmpty()) {
      throw new UserNotFoundException();
    }
    return (User) users.get(0);
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
