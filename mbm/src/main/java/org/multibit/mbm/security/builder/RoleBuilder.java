package org.multibit.mbm.security.builder;

import com.google.common.collect.Lists;
import org.multibit.mbm.security.dto.Authority;
import org.multibit.mbm.security.dto.Role;
import org.springframework.util.Assert;

import java.util.List;

/**
 *  <p>Builder to provide the following to {@link org.multibit.mbm.security.dto.Role}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class RoleBuilder {

  private String name;
  private String description;
  private List<AddAuthority> addAuthorities = Lists.newArrayList();

  private boolean isBuilt= false;

  /**
   * @return A new instance of the builder
   */
  public static RoleBuilder getInstance() {
    return new RoleBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public Role build() {
    validateState();

    // Role is a DTO and so requires a default constructor
    Role role = new Role();

    // Validate mandatory fields
    Assert.notNull(name, "name cannot be null");
    Assert.notNull(description, "description cannot be null");

    role.setName(name);
    role.setDescription(description);

    for (AddAuthority addAuthority: addAuthorities) {
      addAuthority.applyTo(role);
    }

    isBuilt = true;

    return role;
  }

  /**
   * Ensures that the builder is not in an inconsistent state
   */
  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  public RoleBuilder addAuthority(Authority authority) {

    validateState();
    
    addAuthorities.add(new AddAuthority(authority));

    return this;
  }

  public RoleBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public RoleBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into an administrator
   * @return The builder
   */
  public RoleBuilder addAdminAuthorities() {

    // Assume that an admin has all authorities
    for (Authority authority: Authority.values()) {
      addAuthority(authority);
    }

    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into a Customer
   * @return The builder
   */
  public RoleBuilder addCustomerAuthorities() {

    // Assume that a Customer has all external authorities
    for (Authority authority: Authority.values()) {
      if (!authority.isInternal()) {
        addAuthority(authority);
      }
    }

    return this;
  }

  /**
   * Handles adding a new Authority to the Role
   */
  private class AddAuthority {
    private final Authority authority;

    private AddAuthority(Authority authority) {
      this.authority = authority;
    }

    void applyTo(Role role) {

      role.getAuthorities().add(authority);

    }
  }

}
