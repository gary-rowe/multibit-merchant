package org.multibit.mbm.api.request.item;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.*;

import java.util.Map;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to update the details of an existing Item by an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class AdminUpdateItemRequest extends AdminCreateItemRequest {

  /**
   * The GTIN for the Item
   */
  @JsonProperty
  private String gtin = null;

  /**
   * This collection is effectively the fields for the Item
   */
  @JsonProperty
  private Map<ItemField, ItemFieldDetail> itemFieldMap = Maps.newLinkedHashMap();

  public String getGTIN() {
    return gtin;
  }

  public void setGTIN(String gtin) {
    this.gtin = gtin;
  }

  public Map<ItemField, ItemFieldDetail> getItemFieldMap() {
    return itemFieldMap;
  }

  public void setItemFieldMap(Map<ItemField, ItemFieldDetail> itemFieldMap) {
    this.itemFieldMap = itemFieldMap;
  }
}
