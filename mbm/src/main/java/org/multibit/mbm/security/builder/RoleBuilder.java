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
   * <p>Admin are staff members associated with maintaining the application</p>
   * @return The builder
   */
  public RoleBuilder addAdminAuthorities() {

    // TODO Currently assume that an admin has all authorities for convenience - this will change
    for (Authority authority: Authority.values()) {
      addAuthority(authority);
    }

    return this;
  }

  /**
   * <p>Sales are staff members associated with dealing directly with customers</p> 
   * @return The builder
   */
  public RoleBuilder addSalesAuthorities() {

    // Apply a pick list
    addAuthority(Authority.ROLE_SALES);
    addAuthority(Authority.RETRIEVE_INVOICES);
    addAuthority(Authority.RETRIEVE_ORDERS);

    return this;
  }

  /**
   * <p>Sales are staff members associated with dealing directly with customers</p> 
   * <p>Manager has more authorities within this group</p>
   * @return The builder
   */
  public RoleBuilder addSalesManagerAuthorities() {

    // Apply a pick list
    addAuthority(Authority.ROLE_SALES);
    addAuthority(Authority.RETRIEVE_INVOICES);
    addAuthority(Authority.UPDATE_INVOICES);
    addAuthority(Authority.RETRIEVE_ORDERS);
    addAuthority(Authority.UPDATE_ORDERS);


    return this;
  }

  /**
   * <p>Marketing are staff members associated with raising awareness of items through campaigns and writing copy</p> 
   * @return The builder
   */
  public RoleBuilder addMarketingAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Marketing are staff members associated with raising awareness of items through campaigns and writing copy</p> 
   * <p>Manager has more authorities within this group</p>
   * @return The builder
   */
  public RoleBuilder addMarketingManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Stores are staff members associated with inventory</p> 
   * @return The builder
   */
  public RoleBuilder addStoresAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Stores are staff members associated with inventory</p> 
   * <p>Manager has more authorities within this group</p>
   * @return The builder
   */
  public RoleBuilder addStoresManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Finance are staff members associated with accounting and financial reporting</p> 
   * @return The builder
   */
  public RoleBuilder addFinanceAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Finance are staff members associated with accounting and financial reporting</p> 
   * <p>Manager has more authorities within this group</p>
   * @return The builder
   */
  public RoleBuilder addFinanceManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Delivery are staff members associated with getting inventory to the customer</p>
   * @return The builder
   */
  public RoleBuilder addDeliveryAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Delivery are staff members associated with getting inventory to the customer</p>
   * <p>Manager has more authorities within this group</p>
   * @return The builder
   */
  public RoleBuilder addDeliveryManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into a Customer
   * @return The builder
   */
  public RoleBuilder addCustomerAuthorities() {

    // TODO Assume that a Customer has all external authorities - this may change with Affiliate concept
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
