package org.multibit.mbm.web.rest.v1.catalog;

import org.multibit.mbm.catalog.dto.Item;

/**
 * Parameter object to provide query state for Items
 */
public class ItemPagedQuery {
  private final int firstResult;
  private final int maxResults;
  private final Item item;

  /**
   * 
   * @param firstResult The first result position (0-based)
   * @param maxResults The max results limit
   * @param item An example Item that will be used as the basis for selection
   */
  public ItemPagedQuery(int firstResult, int maxResults, Item item) {
    this.firstResult = firstResult;
    this.maxResults = maxResults;
    this.item = item;
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

  /**
   * @return The example Item
   */
  public Item getItem() {
    return item;
  }
}
