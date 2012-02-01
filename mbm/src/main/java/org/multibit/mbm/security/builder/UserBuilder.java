package org.multibit.mbm.security.builder;

import com.google.common.collect.Lists;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.security.dto.*;

import java.util.List;
import java.util.UUID;

/**
 *  <p>Builder to provide the following to {@link org.multibit.mbm.security.dto.User}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class UserBuilder {

  private String openId;
  private String uuid= UUID.randomUUID().toString();
  private List<AddContactMethod> addContactMethods = Lists.newArrayList();
  private List<AddRole> addRoles = Lists.newArrayList();
  private String username;
  private String password;
  private Customer customer;

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static UserBuilder getInstance() {
    return new UserBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public User build() {
    validateState();

    // User is a DTO and so requires a default constructor
    User user = new User();

    user.setOpenId(openId);

    if (uuid == null) {
      throw new IllegalStateException("UUID cannot be null");
    }
    user.setUUID(uuid);

    user.setUsername(username);

    if (password != null) {
      // Digest the plain password
      String encryptedPassword = new StrongPasswordEncryptor().encryptPassword(password);
      user.setPassword(encryptedPassword);
    }

    user.setCustomer(customer);

    for (AddRole addRole : addRoles) {
      addRole.applyTo(user);
    }

    for (AddContactMethod addContactMethod : addContactMethods) {
      addContactMethod.applyTo(user);
    }

    isBuilt = true;

    return user;
  }

  /**
   * @param openId The openId (e.g. "abc123")
   *
   * @return The builder
   */
  public UserBuilder setOpenId(String openId) {
    validateState();
    this.openId = openId;
    return this;
  }

  /**
   * @param uuid The UUID (e.g. "1234-5678")
   *
   * @return The builder
   */
  public UserBuilder setUUID(String uuid) {
    validateState();
    this.uuid = uuid;
    return this;
  }

  public UserBuilder addContactMethod(ContactMethod contactMethod, String detail) {

    addContactMethods.add(new AddContactMethod(contactMethod, detail));

    return this;
  }

  public UserBuilder addRole(Role role) {

    addRoles.add(new AddRole(role));
    return this;
  }

  public UserBuilder addRoles(List<Role> roles) {

    for (Role role : roles) {
      addRoles.add(new AddRole(role));
    }

    return this;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  public UserBuilder setUsername(String username) {
    this.username = username;
    return this;
  }

  public UserBuilder setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * Add the Customer to the User (one permitted)
   *
   * @return The builder
   */
  public UserBuilder addCustomer(Customer customer) {

    this.customer = customer;
    return this;
  }

  /**
   * Handles adding a new contact method to the user
   */
  private class AddContactMethod {
    private final ContactMethod contactMethod;
    private final String detail;

    private AddContactMethod(ContactMethod contactMethod, String detail) {
      this.contactMethod = contactMethod;
      this.detail = detail;
    }

    void applyTo(User user) {
      ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
      contactMethodDetail.setPrimaryDetail(detail);

      user.setContactMethodDetail(contactMethod, contactMethodDetail);

    }
  }

  /**
   * Handles adding a new contact method to the user
   */
  private class AddRole {
    private final Role role;

    private AddRole(Role role) {
      this.role = role;
    }

    void applyTo(User user) {

      UserRole userRole = new UserRole();

      UserRole.UserRolePk userRolePk = new UserRole.UserRolePk();
      userRolePk.setUser(user);
      userRolePk.setRole(role);

      userRole.setPrimaryKey(userRolePk);

      user.getUserRoles().add(userRole);

    }
  }

}
