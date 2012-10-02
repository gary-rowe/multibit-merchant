package org.multibit.mbm.api.request.user;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.multibit.mbm.db.dto.*;

import javax.persistence.*;
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

  /**
   * <p>UUID to allow public User reference without
   * revealing a sequential ID that could be guessed.
   * Typically used as an API key</p>
   */
  @JsonProperty
  private String uuid = null;

  /**
   * <p>Used as a shared secret between this user and the application. Typically
   * part of an HMAC authentication scheme.</p>
   */
  @JsonProperty
  private String secretKey = null;

  @JsonProperty
  private Map<ContactMethod, ContactMethodDetail> contactMethodMap = Maps.newLinkedHashMap();

  /**
   * Manages password resets (optional)
   */
  @JsonProperty
  private DateTime passwordResetAt = null;

  /**
   * Tracks when the user was created (optional for anonymity reasons)
   */
  @Column(name = "created_at", nullable = true)
  private DateTime createdAt = null;

  /**
   * How the User wishes to be displayed to the public (optional for anonymity reasons)
   */
  @JsonProperty
  private String publicName = null;

  /**
   * Indicates if the User has been locked (defaults to unlocked)
   */
  @JsonProperty
  private boolean locked = false;

  /**
   * Indicates if the User is a staff member (defaults to no)
   */
  @JsonProperty
  private boolean staffMember = false;

  /**
   * A User may be linked to a Customer (bi-directional)
   */
  @JsonProperty
  private Customer customer = null;

  /**
   * This collection provides additional optional fields so can be lazy
   */
  @JsonProperty
  private Map<UserField, UserFieldDetail> userFieldMap = Maps.newLinkedHashMap();

  @JsonProperty
  private Set<UserRole> userRoles = Sets.newLinkedHashSet();

}
