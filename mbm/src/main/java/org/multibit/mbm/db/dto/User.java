package org.multibit.mbm.db.dto;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.multibit.mbm.auth.Authority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>DTO to provide the following to application:</p>
 * <ul>
 * <li>Storage of state for the User entity</li>
 * </ul>
 * <p>This is the main server side User entity, it extends the much simplified client side
 * User entity </p>
 *
 * @since 0.0.1
 *         
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
  private static final long serialVersionUID = 38345280321234L;

  /**
   * Numerical ID to allow faster indexing (for internal use)
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  protected Long id = null;

  /**
   * <p>UUID to allow public User reference without
   * revealing a sequential ID that could be guessed.
   * Typically used as an API key</p>
   */
  @Column(name = "api_key", nullable = false)
  protected String apiKey = null;

  /**
   * <p>Used as a shared secret to authenticate this user to the upstream server. Typically
   * part of an HMAC authentication scheme.</p>
   */
  @Column(name = "secret_key", nullable = true)
  protected String secretKey = null;

  /**
   * <p>A user password (not plaintext and optional for anonymity reasons)</p>
   */
  @Column(name = "password", nullable = true)
  protected String passwordDigest = null;

  /**
   * <p>A username (optional for anonymity reasons)</p>
   */
  @Column(name = "username", nullable = true)
  protected String username = null;

  /**
   * A shared secret between this client the user's browser that is revoked when the session ends
   */
  @Column(name = "session_key", nullable = true)
  private String sessionKey;

  @OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  @MapKeyEnumerated()
  private Map<ContactMethod, ContactMethodDetail> contactMethodMap = Maps.newLinkedHashMap();

  /**
   * Manages password resets (optional)
   */
  @Column(name = "password_reset_at", nullable = true)
  private DateTime passwordResetAt = null;


  /**
   * Tracks when the user was created (optional for anonymity reasons)
   */
  @Column(name = "created_at", nullable = true)
  private DateTime createdAt = null;

  /**
   * Indicates if the User has been locked (defaults to unlocked)
   */
  @Column(name = "locked", nullable = false)
  private boolean locked = false;

  /**
   * Indicates if the User has been deleted (archived)
   */
  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  /**
   * Provides a reason for being deleted
   */
  @Column(name = "reasonForDelete", nullable = true)
  private String reasonForDelete = null;

  /**
   * Indicates if the User is a staff member (defaults to no)
   */
  @Column(name = "staff", nullable = false)
  private boolean staffMember = false;

  /**
   * A User may be linked to a Customer (bi-directional)
   */
  @OneToOne(optional = true, cascade = CascadeType.ALL)
  private Customer customer = null;

  /**
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The public API key
   */
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * @return The private shared secret for upstream communications
   */
  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  /**
   * @return The user name to authenticate with the client
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return The digested password to authenticate with the client
   */
  public String getPasswordDigest() {
    return passwordDigest;
  }

  /**
   * <h3>Note that it is expected that Jasypt or similar is used prior to storage</h3>
   * @param passwordDigest The password digest
   */
  public void setPasswordDigest(String passwordDigest) {
    this.passwordDigest = passwordDigest;
  }

  /**
   * @return The session key
   */
  public String getSessionKey() {
    return sessionKey;
  }

  public void setSessionKey(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  /**
   * This collection provides additional optional fields so can be lazy
   */
  @OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    orphanRemoval = true
  )
  @MapKeyEnumerated
  private Map<UserField, UserFieldDetail> userFieldMap = Maps.newLinkedHashMap();

  @OneToMany(
    targetEntity = UserRole.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.user",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  private Set<UserRole> userRoles = Sets.newLinkedHashSet();

  /**
   * Default constructor for Hibernate
   */
  public User() {
  }

  /**
   * @return The {@link ContactMethod} map
   */
  public Map<ContactMethod, ContactMethodDetail> getContactMethodMap() {
    return contactMethodMap;
  }

  public void setContactMethodMap(Map<ContactMethod, ContactMethodDetail> contactMethodMap) {
    this.contactMethodMap = contactMethodMap;
  }

  /**
   * @param contactMethod The contact method (e.g. "EMAIL", "VOIP")
   *
   * @return The {@link ContactMethodDetail} providing the information, or null if none available
   */
  @Transient
  public ContactMethodDetail getContactMethodDetail(ContactMethod contactMethod) {
    return contactMethodMap.get(contactMethod);
  }

  /**
   * @param contactMethod       The contact method (e.g. "EMAIL", "VOIP")
   * @param contactMethodDetail The contact method details providing the email address, or VOIP address etc
   */
  @Transient
  public void setContactMethodDetail(ContactMethod contactMethod, ContactMethodDetail contactMethodDetail) {
    contactMethodMap.put(contactMethod, contactMethodDetail);
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

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

  public DateTime getPasswordResetAt() {
    return passwordResetAt;
  }

  public void setPasswordResetAt(DateTime passwordResetAt) {
    this.passwordResetAt = passwordResetAt;
  }

  public boolean isStaffMember() {
    return staffMember;
  }

  public void setStaffMember(boolean staffMember) {
    this.staffMember = staffMember;
  }

  public Map<UserField, UserFieldDetail> getUserFieldMap() {
    return userFieldMap;
  }

  public void setUserFieldMap(Map<UserField, UserFieldDetail> userFieldMap) {
    this.userFieldMap = userFieldMap;
  }

  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

  @Override
  public String toString() {
    return String.format("User[id=%s, apiKey='%s', secretKey='***']]", id, apiKey);
  }

  /**
   * TODO Consider making this cacheable
   *
   * @param authorities The required authorities
   * @return True if the user has all the required authorities
   *
   */
  public boolean hasAllAuthorities(Authority[] authorities) {
    Set<Authority> requiredAuthorities = Sets.newHashSet(authorities);
    Set<Authority> grantedAuthorities = Sets.newHashSet();
    for (UserRole userRole : userRoles) {
      grantedAuthorities.addAll(userRole.getRole().getAuthorities());
    }

    return grantedAuthorities.containsAll(requiredAuthorities);
  }

  public boolean hasAuthority(Authority authority) {
    return hasAllAuthorities(new Authority[] {authority});
  }
}