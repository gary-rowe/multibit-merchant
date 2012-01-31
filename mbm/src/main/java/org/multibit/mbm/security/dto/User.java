package org.multibit.mbm.security.dto;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;
import org.joda.time.DateTime;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *  <p>DTO to provide the following to application:</p>
 *  <ul>
 *  <li>Storage of state for the User entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
  private static final long serialVersionUID = 38345280321234L;

  @Transient
  private static final Logger log = LoggerFactory.getLogger(User.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  @Column(name = "open_id", nullable = true)
  private String openId = null;

  @Column(name = "uuid", nullable = false)
  private String uuid = null;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
   * A user password (not plaintext and optional for anonymity reasons)
   */
  @Column(name = "password", nullable = true)
  private String password = null;

  /**
   * A username (optional for anonymity reasons)
   */
  @Column(name = "username", nullable = true)
  private String username = null;

  /**
   * How the User wishes to be displayed to the public (optional for anonymity reasons)
   */
  @Column(name = "public_name", nullable = true)
  private String publicName = null;

  /**
   * Indicates if the User has been locked (defaults to unlocked)
   */
  @Column(name = "locked", nullable = false)
  private boolean locked = false;

  /**
   * Indicates if the User is a staff member (defaults to no)
   */
  @Column(name = "staff", nullable = false)
  private boolean staffMember = false;

  /**
   * A User may be linked to a Customer
   */
  @OneToOne(optional = true, cascade = CascadeType.ALL)
  private Customer customer = null;

  /**
   * This collection provides additional optional fields so can be lazy
   */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @MapKeyEnumerated
  private Map<UserField, UserFieldDetail> userFieldMap = Maps.newLinkedHashMap();

  @OneToMany(targetEntity = UserRole.class, cascade = {CascadeType.ALL}, mappedBy = "primaryKey.user", fetch = FetchType.EAGER)
  @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
  private Set<UserRole> userRoles = Sets.newLinkedHashSet();

  /**
   * Default constructor for Hibernate
   */
  public User() {
  }

  /**
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
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
   * @return The UUID associated with this Customer
   */
  public String getUUID() {
    return uuid;
  }

  public void setUUID(String uuid) {
    this.uuid = uuid;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DateTime getPasswordResetAt() {
    return passwordResetAt;
  }

  public void setPasswordResetAt(DateTime passwordResetAt) {
    this.passwordResetAt = passwordResetAt;
  }

  public String getPublicName() {
    return publicName;
  }

  public void setPublicName(String publicName) {
    this.publicName = publicName;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;

    return ObjectUtils.isEqual(
      id, other.id,
      uuid, other.uuid,
      openId, other.openId
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id);
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%s, openId='%s', uuid='%s']]", id, openId, uuid);
  }

}