package org.multibit.mbm.security.dto;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for a User's Roles (link between User and Role)</li>
 * </ul>
 * The UserRole describes the Roles assigned to a particular User
 * </p>
 */
@Entity
@Table(name = "user_roles")
@AssociationOverrides({
  @AssociationOverride(name = "primaryKey.role", joinColumns = @JoinColumn(name = "role_id")),
  @AssociationOverride(name = "primaryKey.user", joinColumns = @JoinColumn(name = "user_id"))})
public class UserRole implements Serializable {

  private static final long serialVersionUID = 1L;

  private UserRolePk primaryKey = new UserRolePk();

  /**
   * Default constructor required by Hibernate
   */
  public UserRole() {}

  /**
   * @param user The User
   * @param role The Role
   */
  public UserRole(User user, Role role) {
    Assert.notNull(user, "user is required");
    Assert.notNull(role, "role is required");
    primaryKey.setUser(user);
    primaryKey.setRole(role);
  }

  @EmbeddedId
  public UserRolePk getPrimaryKey() {
    return primaryKey;
  }

  /**
   * @param primaryKey The primaryKey to set
   */
  public void setPrimaryKey(UserRolePk primaryKey) {
    this.primaryKey = primaryKey;
  }

  /**
   * Transient getter for the associated Role
   *
   * @return returns primaryKey.getRole()
   */
  @Transient
  public Role getRole() {
    return primaryKey.getRole();
  }

  /**
   * @return Shortcut to primaryKey.getUser()
   */
  @Transient
  public User getUser() {
    return primaryKey.getUser();
  }

  /**
   * Shortcut to
   *
   * @return Shortcut to getRole().getName().
   */
  @Transient
  public String getRoleName() {
    return getRole().getName();
  }

  /**
   * Standard primary key class for User to Role supporting many-to-many with attributes
   * relationship.
   */
  @Embeddable
  public static class UserRolePk  implements Serializable {

    private static final long serialVersionUID = 1L;
    private Role role;
    private User user;

    /**
     * @return returns the role
     */
    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "role_id")
    public Role getRole() {
      return role;
    }

    public void setRole(Role role) {
      this.role = role;
    }

    /**
     * @return Returns the user
     */
    @ManyToOne(targetEntity = User.class)
    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

  }
}