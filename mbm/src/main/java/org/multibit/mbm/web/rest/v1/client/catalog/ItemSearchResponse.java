package org.multibit.mbm.web.rest.v1.client.catalog;

import com.google.common.collect.Lists;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.web.rest.v1.client.search.BasePagedQueryResponse;

import java.util.List;

/**
 * <p>Response to provide the following to controllers:</p>
 * <ul>
 * <li>Summary of Item persistent state suitable for clients</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
public class ItemSearchResponse extends BasePagedQueryResponse {

  private List<ItemSummary> itemSummaries=Lists.newArrayList();

  /**
   * Default constructor
   */
  public ItemSearchResponse() {
    super();
  }

  /**
   * @param firstResult The first result position (0-based)
   * @param maxResults  The max results limit
   * @param items        The underlying persistent object collection
   */
  public ItemSearchResponse(int firstResult, int maxResults, List<Item> items) {
    super(maxResults, firstResult);

    for (Item item : items) {
      ItemSummary itemSummary = new ItemSummary(item);
      itemSummaries.add(itemSummary);
    }
    
  }

  public ItemSearchResponse(int firstResult, int maxResults, Item item) {
    super(firstResult, maxResults);
    ItemSummary itemSummary = new ItemSummary(item);
    itemSummaries.add(itemSummary);
  }


  public List<ItemSummary> getItemSummaries() {
    return itemSummaries;
  }

  public void setItemSummaries(List<ItemSummary> itemSummaries) {
    this.itemSummaries = itemSummaries;
  }
}
