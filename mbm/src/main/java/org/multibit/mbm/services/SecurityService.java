package org.multibit.mbm.services;

import com.google.common.collect.Lists;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.db.dto.*;
import org.multibit.mbm.api.request.CreateUserRequest;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dao.UserNotFoundException;
import org.multibit.mbm.util.OpenIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * <p>Service to provide the following to Controllers:</p>
 * <ul>
 * <li>Interaction with the User entity</li>
 * <li>Interaction with the Role entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Service
@Transactional(readOnly = true)
public class SecurityService {

  private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  @Resource(name = "hibernateRoleDao")
  private RoleDao roleDao;

  /**
   * <p>Attempts to insert new Users into the database in response to a successful login</p>
   *
   * @param openId The OpenId that uniquely identifies the User
   *
   * @return The authenticated User
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User persistAuthenticatedUser(String openId) {
    try {
      // Is this a new user?
      return userDao.getUserByOpenId(openId);
    } catch (UserNotFoundException ex) {
      log.debug("Inserting new authenticated user");
      String uuid = UUID.randomUUID().toString();
      User newUser = UserBuilder.newInstance()
        .withOpenId(openId)
        .withUUID(uuid)
        .build();
      return userDao.saveOrUpdate(newUser);
    }

  }

  /**
   * <p>Attempts to insert new Users into the database in response to a successful UUID presentation</p>
   *
   * @param uuid The UUID that uniquely identifies the User
   *
   * @return The anonymous User
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User persistAnonymousUser(String uuid) {
    try {
      // Is this a new user?
      return userDao.getUserByUUID(uuid);
    } catch (UserNotFoundException ex) {
      log.debug("Inserting new anonymous user");
      User newUser = UserBuilder.newInstance()
        .withUUID(uuid)
        .build();
      return userDao.saveOrUpdate(newUser);
    }
  }


  /**
   * <p>Attempt to locate the User using the security Principal</p>
   *
   * TODO Rethink how this is done to keep things DRY
   *
   * @param principal The security principal (if null then null is returned)
   *
   * @return A User or null if not found
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User getUserByPrincipal(Principal principal) {
    User user = null;
    String emailAddress = null;

    if (principal == null) {
      log.warn("Null principal passed in.");
      return null;
    }

    if (principal instanceof OpenIDAuthenticationToken) {
      // Extract information from the OpenId principal
      OpenIDAuthenticationToken token = (OpenIDAuthenticationToken) principal;
      String openId = token.getIdentityUrl();
      List<String> values = OpenIdUtils.getAttributeValuesByName((OpenIDAuthenticationToken) principal, "email");
      if (!values.isEmpty()) {
        emailAddress = values.get(0);
      }

      // Attempt to locate the user
      user = userDao.getUserByOpenId(openId);

      // Check for known primary email address (if supplied)
      ContactMethodDetail contactMethodDetail = user.getContactMethodDetail(ContactMethod.EMAIL);
      if (contactMethodDetail != null && contactMethodDetail.getPrimaryDetail() == null && emailAddress != null) {
        // Fill in the obtained email address
        contactMethodDetail.setPrimaryDetail(emailAddress);
        user = userDao.saveOrUpdate(user);
      }
    }

    if (principal instanceof UsernamePasswordAuthenticationToken) {
      // Extract information from the Basic authentication principal (this will be the UUID)
      UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
      String uuid = token.getPrincipal().toString();

      // Attempt to locate the User
      user = userDao.getUserByUUID(uuid);

    }

    return user;
  }

  /**
   * A
   *
   * @param uuid The UUID
   *
   * @return The User
   */
  public User getUserByUUID(String uuid) {
    return userDao.getUserByUUID(uuid);
  }

  /**
   * Attempts to persist the User
   *
   * @param user The User to persist
   *
   * @return The User
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User persist(User user) {
    return userDao.saveOrUpdate(user);
  }

  /**
   * @return A collection of security roles appropriate for a Customer
   */
  public List<Role> getCustomerRoles() {
    List<Role> customerRoles = Lists.newArrayList();
    customerRoles.add(roleDao.getRoleByAuthority(Authority.ROLE_CUSTOMER));
    return customerRoles;
  }

  /**
   * Create a User as a Customer
   * @param createUserRequest The information required to create and configure the User
   * @return The User
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User createUserAsCustomer(CreateUserRequest createUserRequest) {

    // Build the supporting objects
    Customer customer = CustomerBuilder.newInstance()
      .build();

    // Require the standard Customer Roles
    List<Role> customerRoles = getCustomerRoles();

    // Build the User including the supporting objects
    User newUser = UserBuilder.newInstance()
      .withOpenId(createUserRequest.getOpenId())
      .withUsername(createUserRequest.getUsername())
      .withPassword(createUserRequest.getPassword())
      .withCustomer(customer)
      .withRoles(customerRoles)
      .build();

    // Persist the user
    return persist(newUser);
  }

  /**
   * Package local to allow testing
   *
   * @return The User DAO
   */
  UserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

}
