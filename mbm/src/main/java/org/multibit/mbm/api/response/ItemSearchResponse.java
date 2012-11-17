package org.multibit.mbm.api.response;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.core.model.Item;

import java.util.List;

/**
 * <p>Response to provide the following to controllers:</p>
 * <ul>
 * <li>Summary of Item persistent state suitable for clients</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Deprecated
public class ItemSearchResponse extends BasePagedQueryResponse {

  @JsonProperty
  private List<ItemResponse> itemSummaries=Lists.newArrayList();

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
      ItemResponse itemSummary = new ItemResponse(item);
      itemSummaries.add(itemSummary);
    }
    
  }

  public ItemSearchResponse(int firstResult, int maxResults, Item item) {
    super(firstResult, maxResults);
    ItemResponse itemSummary = new ItemResponse(item);
    itemSummaries.add(itemSummary);
  }


  public List<ItemResponse> getItemSummaries() {
    return itemSummaries;
  }

  public void setItemSummaries(List<ItemResponse> itemSummaries) {
    this.itemSummaries = itemSummaries;
  }
}
