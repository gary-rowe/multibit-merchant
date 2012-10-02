package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.CustomerCreateUserRequest;
import org.multibit.mbm.api.response.hal.AdminUserBridge;
import org.multibit.mbm.api.response.hal.AdminUserCollectionBridge;
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
   * Provide a paged response of all users in the system
   * @param adminUser A user with administrator rights
   * @param rawPageSize The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   * @return A response containing a paged list of all users
   */
  @GET
  @Timed
  @Path("/user")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response getAllByPage(
    @RestrictedTo({Authority.ROLE_ADMIN}) User adminUser,
    @QueryParam("pageSize") Optional<String> rawPageSize,
    @QueryParam("pageNumber") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    List<User> users = userDao.getAllByPage(pageSize, pageNumber);

    AdminUserCollectionBridge bridge = new AdminUserCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, users);

  }

  /**
   * Provide a paged response of all users in the system
   * @param adminUser A user with administrator rights
   * @return A response containing a paged list of all users
   */
  @POST
  @Timed
  @Path("/user")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response createUser(
    @RestrictedTo({Authority.ROLE_ADMIN}) User adminUser, CustomerCreateUserRequest createUserRequest) {

    // Build a user from the given request information
    User user = UserBuilder.newInstance()
      .withUsername(createUserRequest.getUsername())
      .withPassword(createUserRequest.getPassword())
      .withOpenId(createUserRequest.getOpenId())
      .build();

    // Perform basic verification
    Optional<User> verificationUser = userDao.getUserByCredentials(user.getUsername(), user.getPassword());

    if (!verificationUser.isPresent()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    // Persist the user
    User persistentUser = userDao.saveOrUpdate(user);
    AdminUserBridge bridge = new AdminUserBridge(uriInfo, Optional.of(adminUser));

    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getId().toString()).build();

    //return created(bridge, persistentUser, location);
    return created(bridge,persistentUser,location);

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}