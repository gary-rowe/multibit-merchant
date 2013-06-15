package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.common.pagination.PaginatedLists;
import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.domain.repositories.common.EntityReadService;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import javax.annotation.Resource;
import java.sql.SQLException;
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
public abstract class BaseHibernateReadService<T> implements EntityReadService<T> {

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
   * @param clazz The class for the criteria
   *
   * @return The number of rows in the table
   */
  protected Number rowCount(final Class<T> clazz) {
    Number rowCount = (Number) hibernateTemplate.execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        return session
          .createCriteria(clazz)
          .setProjection(Projections.rowCount())
          .uniqueResult();
      }
    });

    Preconditions.checkNotNull(rowCount, "rowCount cannot be null");

    return rowCount;
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

  /**
   * @param pageSize   The page size
   * @param pageNumber The page number
   * @param clazz      The class to which this will apply
   *
   * @return The paginated list
   */
  protected PaginatedList<T> buildPaginatedList(final int pageSize, final int pageNumber, final Class<T> clazz) {

    Number total = rowCount(clazz);

    if (total == null) {
      return PaginatedLists.newPaginatedArrayList(0, 0, Lists.<T>newArrayList());
    }

    int totalPages = (int) Math.ceil(total.doubleValue() / pageSize);

    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session
          .createQuery("from " + clazz.getSimpleName())
          .setMaxResults(pageSize)
          .setFirstResult(pageSize * (pageNumber - 1)); // Apply 0-based index
        return query.list();
      }
    });

    return PaginatedLists.newPaginatedArrayList(pageNumber, totalPages, list);
  }

  @Override
  public PaginatedList<Item> getPaginatedListByExample(int pageSize, int pageNumber, T example) {
    throw new IllegalStateException("Not yet supported for this entity");
  }

}
