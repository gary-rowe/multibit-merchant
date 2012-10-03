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
public abstract class BaseHibernateDao {

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
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <T> Optional<T> first(List list, Class<T> clazz) {
    if (isNotFound(list)) {
      return Optional.absent();
    }
    return Optional.of((T) list.get(0));
  }

  public <T> Optional<T> getById(Class<T> clazz,Long id) {
    T entity = hibernateTemplate.get(clazz, id);
    return Optional.fromNullable(entity);
  }

  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
