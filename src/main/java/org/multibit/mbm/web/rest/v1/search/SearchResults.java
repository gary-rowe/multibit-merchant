package org.multibit.mbm.web.rest.v1.search;

/**
 *  <p>Value object to provide the following to RESTful controllers:</p>
 *  <ul>
 *  <li>Standard search result handling</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */

import java.util.List;

/**
 * Represents the results of a generic search
 */
public class SearchResults<T extends SearchSummary> {
  private final List<T> results;

  private final long start;
  private final boolean lastPage;

  public SearchResults(List<T> results, long start, boolean lastPage) {
    this.results=results;
    this.start=start;
    this.lastPage=lastPage;
  }

  /**
   * Returns the list of matching results
   */
  public List<T> getResults() {
    return results;
  }

  /**
   * @return The starting index of the results
   */
  public long getStart() {
    return start;
  }

  /**
   * @return True if this is the last page of results
   */
  public boolean isLastPage() {
    return lastPage;
  }
}
