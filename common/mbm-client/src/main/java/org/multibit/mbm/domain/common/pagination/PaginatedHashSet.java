package org.multibit.mbm.domain.common.pagination;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link org.multibit.mbm.domain.common.pagination.PaginatedSet} to provide the following to application:</p>
 * <ul>
 * <li>Simple set with pagination</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PaginatedHashSet<T> implements PaginatedSet<T> {

  private final Pagination pagination;

  private final HashSet<T> set;

  public PaginatedHashSet(Pagination pagination, HashSet<T> set) {
    this.pagination = pagination;
    this.set = set;
  }

  public PaginatedHashSet(int pageNumber, int totalPages, HashSet<T> set) {
    this.pagination = new Pagination(pageNumber, totalPages, 0);
    this.set = set;
  }

  @Override
  public Pagination pagination() {
    return this.pagination;
  }

  @Override
  public Set<T> set() {
    return this.set;
  }
}
