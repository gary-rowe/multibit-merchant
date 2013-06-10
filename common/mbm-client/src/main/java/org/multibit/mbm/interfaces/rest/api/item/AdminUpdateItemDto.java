package org.multibit.mbm.interfaces.rest.api.item;

import com.google.common.collect.Maps;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class AdminUpdateItemDto extends AdminCreateItemDto {

  /**
   * The GTIN for the Item
   */
  @JsonProperty
  private String gtin = null;

  /**
   * This collection is effectively the fields for the Item
   */
  @JsonProperty("item_field_map")
  private Map<String, String> itemFieldMap = Maps.newLinkedHashMap();

  public String getGTIN() {
    return gtin;
  }

  public void setGTIN(String gtin) {
    this.gtin = gtin;
  }

  public Map<String, String> getItemFieldMap() {
    return itemFieldMap;
  }

  public void setItemFieldMap(Map<String, String> itemFieldMap) {
    this.itemFieldMap = itemFieldMap;
  }
}
