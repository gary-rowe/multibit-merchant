package org.multibit.mbm.client.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.User;
import org.multibit.mbm.client.domain.model.model.UserBuilder;
import org.multibit.mbm.client.domain.repositories.UserReadService;
import org.multibit.mbm.client.interfaces.rest.api.AdminDeleteEntityDto;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.interfaces.rest.api.representations.hal.user.AdminUserCollectionRepresentation;
import org.multibit.mbm.client.interfaces.rest.api.representations.hal.user.AdminUserRepresentation;
import org.multibit.mbm.client.interfaces.rest.api.user.AdminCreateUserDto;
import org.multibit.mbm.client.interfaces.rest.api.user.AdminUpdateUserDto;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.multibit.mbm.client.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.client.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.client.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link User} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminUserResource extends BaseResource {

  @Resource(name = "hibernateUserDao")
  private UserReadService userReadService;

  /**
   * Create a new User from the given mandatory fields
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the minimum details of the created entity
   */
  @POST
  @Timed
  @Path("/user")
  public Response create(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    AdminCreateUserDto createUserRequest) {

    // Build a new User from the given request information
    User user = UserBuilder.newInstance()
      .withUsername(createUserRequest.getUsername())
      .withPassword(createUserRequest.getPasswordDigest())
      .build();

    // Perform basic verification
    Optional<User> verificationUser = userReadService.getByCredentials(user.getUsername(), user.getPasswordDigest());
    ResourceAsserts.assertNotConflicted(verificationUser, "user");

    // Persist the user
    User persistentUser = userReadService.saveOrUpdate(user);

    // Provide a representation to the client
    Representation representation = new AdminUserRepresentation().get(persistentUser, Optional.of(adminUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getId().toString()).build();

    return created(representation,location);

  }

  /**
   * Provide a paged response of all users in the system
   *
   * @param adminUser     A user with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all users
   */
  @GET
  @Timed
  @Path("/user")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_ADMIN}) User adminUser,
    @QueryParam("ps") Optional<String> rawPageSize,
    @QueryParam("pn") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    PaginatedList<User> users = userReadService.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = new AdminUserCollectionRepresentation().get(users, Optional.of(adminUser));

    return ok(representation);

  }

  /**
   * Update an existing User with the populated fields
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/user/{userId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("userId") Long userId,
    AdminUpdateUserDto updateUserRequest) {

    // Retrieve the user
    Optional<User> user = userReadService.getById(userId);
    ResourceAsserts.assertPresent(user, "user");

    // Verify and apply any changes to the User
    User persistentUser = user.get();

    // Apply the request to the entity
    apply(updateUserRequest, persistentUser);

    // Persist the updated user
    persistentUser = userReadService.saveOrUpdate(user.get());

    // Provide a representation to the client
    Representation representation = new AdminUserRepresentation().get(persistentUser, Optional.of(adminUser));

    return ok(representation);

  }

  /**
   * Delete an existing User (usually meaning set flag to deleted)
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @DELETE
  @Timed
  @Path("/user/{userId}")
  public Response delete(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("userId") Long userId,
    AdminDeleteEntityDto deleteEntityRequest) {

    // Retrieve the user
    Optional<User> user = userReadService.getById(userId);
    ResourceAsserts.assertPresent(user, "user");

    // Verify and apply any changes to the User
    User persistentUser = user.get();
    persistentUser.setDeleted(true);
    persistentUser.setReasonForDelete(deleteEntityRequest.getReason());

    // Persist the updated user
    persistentUser = userReadService.saveOrUpdate(user.get());

    // Provide a representation to the client
    Representation representation = new AdminUserRepresentation().get(persistentUser, Optional.of(adminUser));

    return ok(representation);

  }

  /**
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdateUserDto updateRequest, User entity) {
    // TODO This will fail due to specialised password digest in UserBuilder
    // General approach here should be to use UserBuilder to create a new User
    // then bean copy into the persistent entity ignoring the ID field
    if (updateRequest.getPasswordDigest() != null) {
      entity.setPasswordDigest(updateRequest.getPasswordDigest());
    }
    if (updateRequest.getUsername() != null) {
      entity.setUsername(updateRequest.getUsername());
    }
    if (updateRequest.getApiKey() != null) {
      entity.setApiKey(updateRequest.getApiKey());
    }
    if (updateRequest.getSecretKey() != null) {
      entity.setSecretKey(updateRequest.getSecretKey());
    }
    // TODO Fill in the more advanced entries later (lots of primary/secondary fiddlin' about)
//    for (Map.Entry<String, String> userField: updateRequest.getUserFieldMap().entrySet()) {
//      UserFieldDetail userFieldDetail=new UserFieldDetail();
//      entity.getUserFieldMap().put(userField.getKey(),userField.getValue());
//    }
  }

  public void setUserReadService(UserReadService userReadService) {
    this.userReadService = userReadService;
  }


}