package org.multibit.mbm.api.request.item;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.Column;

/**
 * <p>Base request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state common to all interactions with the Item entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseItemRequest {

  @JsonProperty
  private String sku = null;

  public String getSKU() {
    return sku;
  }

  public void setSKU(String sku) {
    this.sku = sku;
  }

}
