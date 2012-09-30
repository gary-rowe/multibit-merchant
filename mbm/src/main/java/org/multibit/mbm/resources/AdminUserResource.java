package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.AdminUserBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
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
public class AdminUserResource extends BaseResource<List<User>> {

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

    AdminUserBridge bridge = new AdminUserBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, users);

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}