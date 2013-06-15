package org.multibit.mbm.client.interfaces.rest.api.user;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * <p>DTO to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of state for a single User as viewed by the Supplier</li>
 * </ul>
 *
 * TODO Fix all this
 *
 * @since 0.0.1
 *         
 */
public class SupplierUserDto {

  private Map<String,String> optionalProperties= Maps.newHashMap();

  /**
   * @return A collection of optional properties for display. See the API reference for keys.
   */
  public Map<String, String> getOptionalProperties() {
    return optionalProperties;
  }

  public void setOptionalProperties(Map<String, String> optionalProperties) {
    this.optionalProperties = optionalProperties;
  }
}
