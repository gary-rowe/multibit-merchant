package org.multibit.mbm.security.dto;

import org.hibernate.annotations.Cascade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * <p>DTO to provide the following to application:</p>
 * <ul>
 * <li>Groups a number of authorities under a single general heading</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {

  private static final long serialVersionUID = 38452390321234L;

  @Transient
  private static final Logger log = LoggerFactory.getLogger(User.class);

  /**
   * The Role primary key
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
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
   * True if this is an internal staff role
   */
  @Column(name = "internal", nullable = false)
  private boolean internal = true;

  @OneToMany(targetEntity = UserRole.class, cascade = {CascadeType.ALL}, mappedBy = "primaryKey.role")
  @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
  private Set<UserRole> userRoles = new LinkedHashSet<UserRole>();

  /**
   * The authorities associated with this Role
   */
  @ElementCollection(targetClass = Authority.class)
  @CollectionTable(name = "authorities",
    joinColumns = @JoinColumn(name = "role_id"))
  @Column(name = "auth_id")
  private Set<Authority> authorities = new LinkedHashSet<Authority>();

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
