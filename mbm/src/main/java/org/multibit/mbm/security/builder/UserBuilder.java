package org.multibit.mbm.security.builder;

import com.google.common.collect.Lists;
import org.multibit.mbm.customer.builder.CustomerBuilder;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.security.dto.ContactMethod;
import org.multibit.mbm.security.dto.ContactMethodDetail;
import org.multibit.mbm.security.dto.User;

import java.util.List;

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
  private String uuid;
  private List<AddContactMethod> addContactMethods = Lists.newArrayList();
  private String username;
  private String password;
  private Customer customer;

  private boolean isBuilt= false;

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
    user.setPassword(password);
    user.setCustomer(customer);

    for (AddContactMethod addContactMethod: addContactMethods) {
      addContactMethod.applyTo(user);
    }

    isBuilt = true;
    
    return user;
  }

  /**
   * @param openId The openId (e.g. "abc123")
   * @return The builder
   */
  public UserBuilder setOpenId(String openId) {
    validateState();
    this.openId=openId;
    return this;  
  }

  /**
   * @param uuid The UUID (e.g. "1234-5678")
   * @return The builder
   */
  public UserBuilder setUUID(String uuid) {
    validateState();
    this.uuid=uuid;
    return this;
  }

  public UserBuilder addContactMethod(ContactMethod contactMethod, String detail) {
    addContactMethods.add(new AddContactMethod(contactMethod, detail));

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
   * Configure the various supporting structure to make this user into an administrator
   * @return The builder
   */
  public UserBuilder configureAsAdmin() {

    // TODO Add in suitable Roles and Authorities

    return this;
  }

  /**
   * Configure the various supporting structure to make this user into an administrator
   * @return The builder
   */
  public UserBuilder configureAsCustomer() {

    // TODO Add in suitable Roles and Authorities

    customer = CustomerBuilder.
      getInstance()
      .build(); 
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

      user.setContactMethodDetail(contactMethod,contactMethodDetail);

    }
  }

}
