package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.User;
import org.multibit.mbm.client.domain.repositories.UserReadService;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateUserDao")
public class HibernateUserReadService extends BaseHibernateReadService<User> implements UserReadService {

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

  @Override
  public PaginatedList<User> getPaginatedList(final int pageSize, final int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, User.class);
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
