package org.multibit.mbm.web.rest.v1.client.catalog;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.web.rest.v1.client.search.BasePagedQueryResponse;

/**
 * Parameter object to provide query state for Items
 */
public class ItemPagedQueryResponse extends BasePagedQueryResponse {
  private final Item item;

  /**
   * 
   * @param firstResult The first result position (0-based)
   * @param maxResults The max results limit
   * @param item An example Item that will be used as the basis for selection
   */
  public ItemPagedQueryResponse(int firstResult, int maxResults, Item item) {
    super(maxResults, firstResult);
    this.item = item;
  }

  /**
   * @return The Item
   */
  public Item getItem() {
    return item;
  }
}
