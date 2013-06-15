package org.multibit.mbm.client.common.pagination;

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

  private static final int DEFAULT_RESULTS_PER_PAGE=10;

  private final Pagination pagination;

  private final ArrayList<T> list;

  public PaginatedArrayList(Pagination pagination, ArrayList<T> list) {
    this.pagination = pagination;
    this.list = list;
  }

  public PaginatedArrayList(int pageNumber, int totalPages, ArrayList<T> list) {
    this.pagination = new Pagination(pageNumber, totalPages, DEFAULT_RESULTS_PER_PAGE);
    this.list = list;
  }

  public PaginatedArrayList(int pageNumber, int totalPages, int resultsPerPage, ArrayList<T> list) {
    this.pagination = new Pagination(pageNumber, totalPages, resultsPerPage);
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
