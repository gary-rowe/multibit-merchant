package org.multibit.mbm.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.request.user.WebFormAuthenticationRequest;
import org.multibit.mbm.interfaces.rest.api.request.user.WebFormRegistrationRequest;
import org.multibit.mbm.interfaces.rest.api.response.hal.user.ClientUserBridge;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.domain.repositories.RoleDao;
import org.multibit.mbm.domain.repositories.UserDao;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/client/user")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class ClientUserResource extends BaseResource {

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  @Resource(name = "hibernateRoleDao")
  private RoleDao roleDao;

  /**
   * @param clientUser The client application acting as the proxy for this user
   *
   * @return A HAL representation of the result
   */
  @POST
  @Timed
  @Path("/anonymous")
  public Response anonymousUser(
    @RestrictedTo({Authority.ROLE_CLIENT})
    User clientUser) {

    // Retrieve the role by its authority name
    Optional<Role> optionalRole = roleDao.getByName(Authority.ROLE_PUBLIC.name());
    ResourceAsserts.assertPresent(optionalRole,"role");

    // Build a new Customer for the anonymous user
    Customer customer = CustomerBuilder
      .newInstance()
      .build();

    // Build an anonymous new User
    User user = UserBuilder
      .newInstance()
      .withCustomer(customer)
      .withRole(optionalRole.get())
      .build();

    // Persist the User with cascade for the Customer
    User persistentUser = userDao.saveOrUpdate(user);

    // Provide a minimal representation to the client
    ClientUserBridge bridge = new ClientUserBridge(uriInfo, Optional.of(clientUser));
    URI location = UriBuilder.fromResource(CustomerUserResource.class).build();

    return created(bridge, persistentUser, location);

  }

  /**
   * @param clientUser          The client application acting as the proxy for this user
   * @param registrationRequest The registration request
   *
   * @return A HAL representation of the result
   */
  @POST
  @Timed
  @Path("/register")
  public Response registerUser(
    @RestrictedTo({Authority.ROLE_CLIENT})
    User clientUser,
    WebFormRegistrationRequest registrationRequest) {

    Preconditions.checkNotNull(registrationRequest);
    Preconditions.checkNotNull(registrationRequest.getPasswordDigest());
    Preconditions.checkNotNull(registrationRequest.getUsername());

    // Perform conflict check
    Optional<User> verificationUser = userDao.getByCredentials(
      registrationRequest.getUsername(),
      registrationRequest.getPasswordDigest());
    ResourceAsserts.assertNotConflicted(verificationUser, "user");

    // Build a new Customer
    Customer customer = CustomerBuilder
      .newInstance()
      .build();

    // Build a new User
    User user = UserBuilder
      .newInstance()
      .withUsername(registrationRequest.getUsername())
      .withPassword(registrationRequest.getPasswordDigest())
        // TODO Fill in the rest of the registration details
      .withCustomer(customer)
      .build();

    // Persist the User with cascade for the Customer
    User persistentUser = userDao.saveOrUpdate(user);

    // Provide a minimal representation to the client
    ClientUserBridge bridge = new ClientUserBridge(uriInfo, Optional.of(clientUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getApiKey()).build();

    return created(bridge, persistentUser, location);

  }

  /**
   * @param clientUser            The client application acting as the proxy for this user
   * @param authenticationRequest The authentication request
   *
   * @return A HAL representation of the result
   */
  @POST
  @Timed
  @Path("/authenticate")
  public Response authenticateUser(
    @RestrictedTo({Authority.ROLE_CLIENT})
    User clientUser,
    WebFormAuthenticationRequest authenticationRequest) {

    Optional<User> requestedUser = userDao.getByCredentials(authenticationRequest.getUsername(), authenticationRequest.getPasswordDigest());

    ClientUserBridge bridge = new ClientUserBridge(uriInfo, Optional.of(clientUser));

    // The bridge can handle a null
    return ok(bridge, requestedUser.orNull());

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
  }
}