package org.multibit.mbm.domain.common.pagination;

import com.google.common.base.Preconditions;

/**
 * <p>Value object to provide the following to domain:</p>
 * <ul>
 * <li>Storage of pagination state</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class Pagination {

  private final int currentPage;
  private final int totalPages;

  public Pagination(int currentPage, int totalPages) {
    Preconditions.checkState(totalPages > 0, "'totalPages' must be greater than zero");
    Preconditions.checkState(currentPage > 0, "'currentPage' must be greater than zero");
    this.currentPage = currentPage;
    this.totalPages = totalPages;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getNextPage() {
    return currentPage >= totalPages ? totalPages : currentPage + 1;
  }

  public int getPreviousPage() {
    return currentPage == 1 ? currentPage : currentPage - 1;
  }

}
