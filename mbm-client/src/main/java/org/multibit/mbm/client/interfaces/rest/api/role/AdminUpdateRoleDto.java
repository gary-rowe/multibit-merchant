package org.multibit.mbm.client.interfaces.rest.api.role;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to update the details of an existing Item by an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class AdminUpdateRoleDto extends AdminCreateRoleDto {

  /**
   * Indicates if the User has been deleted (archived)
   */
  @JsonProperty
  private boolean deleted = false;

  /**
   * Provides a reason for being deleted
   */
  @JsonProperty("reason_for_delete")
  private String reasonForDelete = null;

  /**
   * True if this is an internal staff role
   */
  @JsonProperty
  private boolean internal = true;

  /**
   * The authorities to be bound to the Role
   */
//  @JsonProperty
//  private Set<String> authorities = Sets.newLinkedHashSet();

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getReasonForDelete() {
    return reasonForDelete;
  }

  public void setReasonForDelete(String reasonForDelete) {
    this.reasonForDelete = reasonForDelete;
  }

  public boolean isInternal() {
    return internal;
  }

  public void setInternal(boolean internal) {
    this.internal = internal;
  }

//  public Set<String> getAuthorities() {
//    return authorities;
//  }
//
//  public void setAuthorities(Set<String> authorities) {
//    this.authorities = authorities;
//  }
}
