package org.multibit.mbm.db.dto;

import com.google.common.collect.Lists;
import org.multibit.mbm.auth.Authority;
import org.springframework.util.Assert;

import java.util.List;

/**
 *  <p>Builder to provide the following to {@link org.multibit.mbm.db.dto.Role}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class RoleBuilder {

  private String name;
  private String description;
  private List<AddAuthority> addAuthorities = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static RoleBuilder newInstance() {
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

    for (AddAuthority addAuthority : addAuthorities) {
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

  /**
   * @param authority The authority to add
   *
   * @return The builder
   */
  public RoleBuilder withAuthority(Authority authority) {
    addAuthorities.add(new AddAuthority(authority));
    return this;
  }

  /**
   * @param name The name of the role
   *
   * @return The builder
   */
  public RoleBuilder withName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @param description The role description
   *
   * @return The builder
   */
  public RoleBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * <p>Admin are staff members associated with maintaining the application</p>
   *
   * @return The builder
   */
  public RoleBuilder withAdminAuthorities() {

    // TODO Currently assume that an admin has all authorities for convenience - this will change
    for (Authority authority : Authority.values()) {
      withAuthority(authority);
    }

    return this;
  }

  /**
   * <p>Sales are staff members associated with dealing directly with customers</p>
   *
   * @return The builder
   */
  public RoleBuilder withSalesAuthorities() {

    // Apply a pick list
    withAuthority(Authority.ROLE_SALES);
    withAuthority(Authority.RETRIEVE_INVOICES);
    withAuthority(Authority.RETRIEVE_ORDERS);

    return this;
  }

  /**
   * <p>Sales are staff members associated with dealing directly with customers</p>
   * <p>Manager has more authorities within this group</p>
   *
   * @return The builder
   */
  public RoleBuilder withSalesManagerAuthorities() {

    // Apply a pick list
    withAuthority(Authority.ROLE_SALES);
    withAuthority(Authority.RETRIEVE_INVOICES);
    withAuthority(Authority.UPDATE_INVOICES);
    withAuthority(Authority.RETRIEVE_ORDERS);
    withAuthority(Authority.UPDATE_ORDERS);


    return this;
  }

  /**
   * <p>Marketing are staff members associated with raising awareness of items through campaigns and writing copy</p>
   *
   * @return The builder
   */
  public RoleBuilder withMarketingAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Marketing are staff members associated with raising awareness of items through campaigns and writing copy</p>
   * <p>Manager has more authorities within this group</p>
   *
   * @return The builder
   */
  public RoleBuilder withMarketingManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Stores are staff members associated with inventory</p>
   *
   * @return The builder
   */
  public RoleBuilder withStoresAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Stores are staff members associated with inventory</p>
   * <p>Manager has more authorities within this group</p>
   *
   * @return The builder
   */
  public RoleBuilder withStoresManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Finance are staff members associated with accounting and financial reporting</p>
   *
   * @return The builder
   */
  public RoleBuilder withFinanceAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Finance are staff members associated with accounting and financial reporting</p>
   * <p>Manager has more authorities within this group</p>
   *
   * @return The builder
   */
  public RoleBuilder withFinanceManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Delivery are staff members associated with getting inventory to the customer</p>
   *
   * @return The builder
   */
  public RoleBuilder withDeliveryAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * <p>Delivery are staff members associated with getting inventory to the customer</p>
   * <p>Manager has more authorities within this group</p>
   *
   * @return The builder
   */
  public RoleBuilder withDeliveryManagerAuthorities() {

    // TODO Apply a pick list

    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into an authenticated Customer
   *
   * @return The builder
   */
  public RoleBuilder withCustomerAuthorities() {

    withAuthority(Authority.ROLE_PUBLIC);
    withAuthority(Authority.ROLE_PARTIAL);
    withAuthority(Authority.ROLE_CUSTOMER);

    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into an anonymous
   * or un-authenticated Customer
   *
   * @return The builder
   */
  public RoleBuilder withPublicAuthorities() {

    withAuthority(Authority.ROLE_PUBLIC);
    withAuthority(Authority.ROLE_PARTIAL);

    return this;
  }

  /**
   * Configure the various supporting structure to make this Role into a Client application
   *
   * @return The builder
   */
  public RoleBuilder withClientAuthorities() {

    withAuthority(Authority.ROLE_PUBLIC);
    withAuthority(Authority.ROLE_CLIENT);

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
