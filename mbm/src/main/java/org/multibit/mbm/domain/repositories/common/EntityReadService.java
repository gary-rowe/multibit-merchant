package org.multibit.mbm.domain.repositories.common;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;

/**
 * <p>Interface to provide the following to entity repositories:</p>
 * <ul>
 * <li>Provision of common read methods</li>
 * </ul>
 * <p>This approach is in line with the <a href="http://martinfowler.com/bliki/CQRS.html">Command and Query Responsibility Segregration (CQRS)</a> strategic pattern by Greg Young.</p>
 *
 * @since 0.0.1
 *         
 */
public interface EntityReadService<T> {

  /**
   * Attempt to locate the entity by its ID
   *
   * @param id The ID
   *
   * @return A matching entity
   */
  Optional<T> getById(Long id);

  /**
   * Provide a {@link org.multibit.mbm.domain.common.pagination.PaginatedList} list of a subset of the entity
   *
   * @param pageSize   The total entities returned in one page (1-based)
   * @param pageNumber The page number (1-based)
   * @return A {@link org.multibit.mbm.domain.common.pagination.PaginatedList} with the entities
   */
  PaginatedList<T> getPaginatedList(final int pageSize, final int pageNumber);

  /**
   * Provide a paged list of all Items filtered by an example
   *
   * @param pageSize   The total record in one page (1-based)
   * @param pageNumber The page number (1-based)
   * @param example    An example containing fields to match on (nulls are wildcards)
   *
   * @return A {@link org.multibit.mbm.domain.common.pagination.PaginatedList} with the matching entities
   */
  PaginatedList<Item> getPaginatedListByExample(final int pageSize, final int pageNumber, final T example);

}
