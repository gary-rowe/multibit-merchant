package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.user.AdminCreateUserRequest;
import org.multibit.mbm.api.request.user.AdminUpdateUserRequest;
import org.multibit.mbm.api.response.hal.user.AdminUserBridge;
import org.multibit.mbm.api.response.hal.user.AdminUserCollectionBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.resources.BaseResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link User} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Path("/admin")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminUserResource extends BaseResource {

  UserDao userDao;

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
    AdminCreateUserRequest createUserRequest) {

    // Build a user from the given request information
    User user = UserBuilder.newInstance()
      .withUsername(createUserRequest.getUsername())
      .withPassword(createUserRequest.getPassword())
      .withOpenId(createUserRequest.getOpenId())
      .build();

    // Perform basic verification
    Optional<User> verificationUser = userDao.getByCredentials(user.getUsername(), user.getPassword());

    if (!verificationUser.isPresent()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    // Persist the user
    User persistentUser = userDao.saveOrUpdate(user);

    // Provide a representation to the client
    AdminUserBridge bridge = new AdminUserBridge(uriInfo, Optional.of(adminUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getId().toString()).build();

    return created(bridge, persistentUser, location);

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
    @QueryParam("pageSize") Optional<String> rawPageSize,
    @QueryParam("pageNumber") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    List<User> users = userDao.getAllByPage(pageSize, pageNumber);

    // Provide a representation to the client
    AdminUserCollectionBridge bridge = new AdminUserCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, users);

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
    AdminUpdateUserRequest updateUserRequest) {

    // Retrieve the user
    Optional<User> user = userDao.getById(userId);

    if (!user.isPresent()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    // Verify and apply any changes to the User
    // TODO Fill in all details
    User persistentUser = user.get();
    persistentUser.setPassword(updateUserRequest.getPassword());
    persistentUser.setUsername(updateUserRequest.getUsername());
    persistentUser.setOpenId(updateUserRequest.getOpenId());

    // Persist the updated user
    persistentUser = userDao.saveOrUpdate(user.get());

    // Provide a representation to the client
    AdminUserBridge bridge = new AdminUserBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentUser);

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
    AdminDeleteEntityRequest deleteEntityRequest) {

    // Retrieve the user
    Optional<User> user = userDao.getById(userId);

    if (!user.isPresent()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    // Verify and apply any changes to the User
    User persistentUser = user.get();
    persistentUser.setDeleted(true);
    persistentUser.setReasonForDelete(deleteEntityRequest.getReason());

    // Persist the updated user
    persistentUser = userDao.saveOrUpdate(user.get());

    // Provide a representation to the client
    AdminUserBridge bridge = new AdminUserBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentUser);

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}