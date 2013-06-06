package org.multibit.mbm.interfaces.rest.api.request.user;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Set;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to update the details of an existing User by an administrator</li>
 * </ul>
 * <p>When an administrator updates a User there is a lot more detail that can be added that is
 * not available to the general public.</p>
 *
 * @since 0.0.1
 *         
 */
public class AdminUpdateUserRequest extends AdminCreateUserRequest {


  @JsonProperty("contact_method_map")
  private Map<String, String> contactMethodMap = Maps.newLinkedHashMap();

  /**
   * Manages password resets (optional)
   */
  @JsonProperty("password_reset_at")
  private DateTime passwordResetAt = null;

  /**
   * Tracks when the user was created (optional for anonymity reasons)
   */
  @JsonProperty("created_at")
  private DateTime createdAt = null;

  /**
   * Indicates if the User has been locked (defaults to unlocked)
   */
  @JsonProperty
  private boolean locked = false;

  /**
   * Indicates if the User is a staff member (defaults to no)
   */
  @JsonProperty("staff_member")
  private boolean staffMember = false;

  /**
   * A User may be linked to a Customer (bi-directional)
   */
  @JsonProperty
  private String customer = null;

  /**
   * This collection provides additional optional fields so can be lazy
   */
  @JsonProperty("user_field_map")
  private Map<String, String> userFieldMap = Maps.newLinkedHashMap();

  @JsonProperty("user_roles")
  private Set<String> userRoles = Sets.newLinkedHashSet();

  public Map<String, String> getContactMethodMap() {
    return contactMethodMap;
  }

  public void setContactMethodMap(Map<String, String> contactMethodMap) {
    this.contactMethodMap = contactMethodMap;
  }

  public DateTime getPasswordResetAt() {
    return passwordResetAt;
  }

  public void setPasswordResetAt(DateTime passwordResetAt) {
    this.passwordResetAt = passwordResetAt;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public boolean isStaffMember() {
    return staffMember;
  }

  public void setStaffMember(boolean staffMember) {
    this.staffMember = staffMember;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public Map<String, String> getUserFieldMap() {
    return userFieldMap;
  }

  public void setUserFieldMap(Map<String, String> userFieldMap) {
    this.userFieldMap = userFieldMap;
  }

  public Set<String> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<String> userRoles) {
    this.userRoles = userRoles;
  }
}
