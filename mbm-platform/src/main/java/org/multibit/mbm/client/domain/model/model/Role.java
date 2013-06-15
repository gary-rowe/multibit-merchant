package org.multibit.mbm.client.domain.model.model;

import com.google.common.collect.Sets;
import org.multibit.mbm.client.common.Identifiable;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


/**
 * <p>DTO to provide the following to application:</p>
 * <ul>
 * <li>Groups a number of authorities under a single general heading</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Entity
@Table(name = "roles")
public class Role implements Identifiable, Serializable {

  private static final long serialVersionUID = 38452390321234L;

  /**
   * The Role primary key
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * The Role name (e.g. "ROLE_ADMIN")
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * The description (e.g. "Internal role provided to certain staff members")
   */
  @Column(name = "description", nullable = false)
  private String description = null;

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
   * True if this is an internal staff role
   */
  @Column(name = "internal", nullable = false)
  private boolean internal = true;

  @OneToMany(
    targetEntity = UserRole.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.role",
    orphanRemoval = true
  )
  private Set<UserRole> userRoles = Sets.newLinkedHashSet();

  /**
   * The authorities associated with this Role
   * Note the use of the String enumeration rather than ordinal. As the database grows the enum ordinals
   * may change which will make mapping them rather hard between database versions.
   */
  @Enumerated(value = EnumType.STRING)
  @ElementCollection(
    targetClass = Authority.class,
    fetch = FetchType.EAGER)
  @CollectionTable(name = "authorities",
    joinColumns = @JoinColumn(name = "role_id"))
  @Column(name = "auth_name" )
  private Set<Authority> authorities = Sets.newLinkedHashSet();

  /**
   * Default constructor for Hibernate
   */
  public Role() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isInternal() {
    return internal;
  }

  public void setInternal(boolean internal) {
    this.internal = internal;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> userRoles) {
    this.userRoles = userRoles;
  }
}
