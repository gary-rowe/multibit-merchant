package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.Item;

/**
 * <p>Parameter object to provide query state for Items</p>
 */
public class ItemPagedQueryResponse extends BasePagedQueryResponse {

  @JsonProperty
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
