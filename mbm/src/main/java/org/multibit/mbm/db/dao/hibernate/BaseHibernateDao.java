package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.springframework.orm.hibernate3.HibernateTemplate;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * <p>Base class to provide the following to Hibernate DAOs:</p>
 * <ul>
 * <li>Provision of useful utility methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseHibernateDao<T> {

  @Resource(name = "hibernateTemplate")
  protected HibernateTemplate hibernateTemplate = null;

  /**
   * @param collection The collection to check
   *
   * @return True if collection is null or empty
   */
  protected boolean isNotFound(Collection collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * @param list The List to check and extract from
   *
   * @return The first entity in the list if present
   */
  @SuppressWarnings("unchecked")
  protected Optional<T> first(List list) {
    if (isNotFound(list)) {
      return Optional.absent();
    }
    // Initialize the first entry
    T entity = (T) list.get(0);
    return Optional.of(initialized(entity));
  }

  /**
   * Initialize various collections since we are targeting the individual entity (perhaps for display)
   *
   * @param entity The entity
   *
   * @return The entity with all collections initialized
   */
  protected abstract T initialized(T entity);

  /**
   * Performs an ID lookup, initializes and wraps in an Optional
   *
   * @param clazz The target class
   * @param id    The primary ID
   *
   * @return An optional and an initialized entity if present
   */
  public Optional<T> getById(Class<T> clazz, Long id) {
    T entity = hibernateTemplate.get(clazz, id);
    // Initialize the entry
    if (entity != null) {
      return Optional.of(initialized(entity));
    }
    return Optional.absent();
  }

  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
