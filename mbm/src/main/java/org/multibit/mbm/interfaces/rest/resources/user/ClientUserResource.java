package org.multibit.mbm.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.domain.repositories.RoleReadService;
import org.multibit.mbm.domain.repositories.UserReadService;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.representations.hal.user.ClientUserRepresentation;
import org.multibit.mbm.interfaces.rest.api.user.WebFormAuthenticationDto;
import org.multibit.mbm.interfaces.rest.api.user.WebFormRegistrationDto;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
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
  private UserReadService userReadService;

  @Resource(name = "hibernateRoleDao")
  private RoleReadService roleReadService;

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
    Optional<Role> optionalRole = roleReadService.getByName(Authority.ROLE_PUBLIC.name());
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
    User persistentUser = userReadService.saveOrUpdate(user);

    // Provide a minimal representation to the client
    Representation representation = new ClientUserRepresentation().get(persistentUser);
    URI location = UriBuilder.fromResource(CustomerUserResource.class).build();

    return created(representation, location);

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
    WebFormRegistrationDto registrationRequest) {

    Preconditions.checkNotNull(registrationRequest);
    Preconditions.checkNotNull(registrationRequest.getPasswordDigest());
    Preconditions.checkNotNull(registrationRequest.getUsername());

    // Perform conflict check
    Optional<User> verificationUser = userReadService.getByCredentials(
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
    User persistentUser = userReadService.saveOrUpdate(user);

    // Provide a minimal representation to the client
    Representation representation = new ClientUserRepresentation().get(persistentUser);
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getApiKey()).build();

    return created(representation, location);

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
    WebFormAuthenticationDto authenticationRequest) {

    Optional<User> requestedUser = userReadService.getByCredentials(authenticationRequest.getUsername(), authenticationRequest.getPasswordDigest());
    ResourceAsserts.assertPresent(requestedUser,"requestedUser");

    Representation representation = new ClientUserRepresentation().get(requestedUser.get());

    return ok(representation);

  }

  public void setUserReadService(UserReadService userReadService) {
    this.userReadService = userReadService;
  }

  public void setRoleReadService(RoleReadService roleReadService) {
    this.roleReadService = roleReadService;
  }
}