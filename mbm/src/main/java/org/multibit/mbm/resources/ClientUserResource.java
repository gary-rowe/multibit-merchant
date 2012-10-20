package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.user.WebFormAuthenticationRequest;
import org.multibit.mbm.api.response.hal.user.CustomerMinimalUserBridge;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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

  /**
   * @param clientUser The client application acting as the proxy for this user
   * @param authenticationRequest    The authentication request
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

    CustomerMinimalUserBridge bridge = new CustomerMinimalUserBridge(uriInfo, Optional.of(clientUser));

    // The bridge can handle a null
    return ok(bridge, requestedUser.orNull());

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}