package org.multibit.mbm.db.dto;

import com.google.common.collect.Lists;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.password.rfc2307.RFC2307SHAPasswordEncryptor;

import java.util.List;
import java.util.UUID;

/**
 *  <p>Builder to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class UserBuilder {

  private final PasswordEncryptor weakPasswordEncryptor = new RFC2307SHAPasswordEncryptor();
  private final PasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();

  private String apiKey = null;
  private String secretKey = null;
  private List<AddContactMethod> addContactMethods = Lists.newArrayList();
  private List<AddRole> addRoles = Lists.newArrayList();
  private String username;
  private String plainPassword;
  private Customer customer;

  private boolean isBuilt = false;

  /**
   * Indicates that repeatable test data should be used
   * This is because it is a nightmare to fully mock the behaviour
   * of this class and this presents a simple alternative
   */
  public static boolean isTestMode = false;

  /**
   * @return A new instance of the builder
   */
  public static UserBuilder newInstance() {
    return new UserBuilder();
  }

  public UserBuilder() {
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public User build() {
    validateState();

    // User is a DTO and so requires a default constructor
    User user = new User();

    // The API key should be a UUID but represented as a String
    // to ease persistence
    if (apiKey == null) {
      apiKey = createApiKey();
    }
    user.setApiKey(apiKey);
    if (secretKey == null) {
      secretKey = createSecretKey();
    }
    user.setSecretKey(secretKey);

    user.setUsername(username);

    // TODO Use Mockito for this
    if (plainPassword != null) {
      // Make a digest of the plain password since that is what will be received
      // during authentication requests
      user.setPasswordDigest(weakPasswordEncryptor.encryptPassword(plainPassword));
      // In test mode we don't use the strong multi-pass digest
      if (!isTestMode) {
        // In production the digest is used to seed the strong multi-pass digest
        user.setPasswordDigest(strongPasswordEncryptor.encryptPassword(user.getPasswordDigest()));
      }
    }

    // Bi-directional relationship
    if (customer != null) {
      user.setCustomer(customer);
      customer.setUser(user);
    }

    for (AddRole addRole : addRoles) {
      addRole.applyTo(user);
    }

    for (AddContactMethod addContactMethod : addContactMethods) {
      addContactMethod.applyTo(user);
    }

    isBuilt = true;

    return user;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  /**
   * @return A suitable API key
   */
  private String createApiKey() {
    return UUID.randomUUID().toString();
  }

  /**
   * @return A suitable secret key
   */
  private String createSecretKey() {
    return createApiKey() + createApiKey();
  }

  /**
   * @param apiKey The public API key (e.g. "1234-5678")
   *
   * @return The builder
   */
  public UserBuilder withApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  /**
   * @param secretKey The secretKey (base64 encoded)
   *
   * @return The builder
   */
  public UserBuilder withSecretKey(String secretKey) {
    this.secretKey = secretKey;
    return this;
  }

  public UserBuilder withContactMethod(ContactMethod contactMethod, String detail) {

    addContactMethods.add(new AddContactMethod(contactMethod, detail));

    return this;
  }

  public UserBuilder withRole(Role role) {

    addRoles.add(new AddRole(role));
    return this;
  }

  public UserBuilder withRoles(List<Role> roles) {

    for (Role role : roles) {
      addRoles.add(new AddRole(role));
    }

    return this;
  }

  public UserBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserBuilder withPassword(String password) {
    this.plainPassword = password;
    return this;
  }

  /**
   * Add the Customer to the User (one permitted)
   *
   * @return The builder
   */
  public UserBuilder withCustomer(Customer customer) {
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
