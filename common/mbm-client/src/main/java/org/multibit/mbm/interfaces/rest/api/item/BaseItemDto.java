package org.multibit.mbm.interfaces.rest.api.item;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Base request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state common to all interactions with the Item entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseItemDto {

  @JsonProperty
  private String sku = null;

  public String getSKU() {
    return sku;
  }

  public void setSKU(String sku) {
    this.sku = sku;
  }

}
