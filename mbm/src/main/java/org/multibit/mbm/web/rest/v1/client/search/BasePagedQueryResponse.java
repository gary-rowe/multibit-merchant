package org.multibit.mbm.web.rest.v1.client.search;

import org.multibit.mbm.web.rest.v1.client.BaseResponse;

/**
 *  <p>Base Response to provide the following to search controllers:</p>
 *  <ul>
 *  <li>Provision of additional state to control page query results</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class BasePagedQueryResponse extends BaseResponse {
  protected final int firstResult;
  protected final int maxResults;

  /**
   * Provide a default constructor for JAXB
   */
  public BasePagedQueryResponse() {
    firstResult=0;
    maxResults=50;
  }

  public BasePagedQueryResponse(int maxResults, int firstResult) {
    this.maxResults = maxResults;
    this.firstResult = firstResult;
  }

  /**
   * @return The zero-based index of the first result
   */
  public int getFirstResult() {
    return firstResult;
  }

  /**
   * @return The maximum number of results to return (limited to > 0 and <= 50)
   */
  public int getMaxResults() {
    return maxResults > 50 ? 50: (maxResults < 0 ? 0: maxResults );
  }
}
