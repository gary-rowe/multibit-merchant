package org.multibit.mbm.api.request.role;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>Base request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state common to all interactions with the Role entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseRoleRequest {

  @JsonProperty
  private String name = null;

  @JsonProperty
  private String description = null;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
