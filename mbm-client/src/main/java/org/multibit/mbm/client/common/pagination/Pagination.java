package org.multibit.mbm.client.common.pagination;

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
  private final int resultsPerPage;

  /**
   * @param currentPage    The 1-based index of the current page
   * @param totalPages     The 1-based count of total pages
   * @param resultsPerPage The 1-based count of results per page
   */
  public Pagination(int currentPage, int totalPages, int resultsPerPage) {
    Preconditions.checkState(totalPages > 0, "'totalPages' must be greater than zero");
    Preconditions.checkState(currentPage > 0, "'currentPage' must be greater than zero");
    Preconditions.checkState(resultsPerPage > 0, "'resultsPerPage' must be greater than zero");
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.resultsPerPage = resultsPerPage;
  }

  /**
   * @return The 1-based index of the current page
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * @return The 1-based count of total pages
   */
  public int getTotalPages() {
    return totalPages;
  }

  /**
   * @return The 1-based count of results per page
   */
  public int getResultsPerPage() {
    return resultsPerPage;
  }

  /**
   * @return The 1-based index of the next page
   */
  public int getNextPage() {
    return currentPage >= totalPages ? totalPages : currentPage + 1;
  }

  /**
   * @return The 1-based index of the previous page
   */
  public int getPreviousPage() {
    return currentPage == 1 ? currentPage : currentPage - 1;
  }

}
