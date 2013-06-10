package org.multibit.mbm.domain.common.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>{@link PaginatedList} to provide the following to application:</p>
 * <ul>
 * <li>Simple list with pagination</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PaginatedArrayList<T> implements PaginatedList<T> {

  private final Pagination pagination;

  private final ArrayList<T> list;

  public PaginatedArrayList(Pagination pagination, ArrayList<T> list) {
    this.pagination = pagination;
    this.list = list;
  }

  public PaginatedArrayList(int pageNumber, int totalPages, ArrayList<T> list) {
    this.pagination = new Pagination(pageNumber, totalPages);
    this.list = list;
  }

  @Override
  public Pagination pagination() {
    return this.pagination;
  }

  @Override
  public List<T> list() {
    return this.list;
  }
}
