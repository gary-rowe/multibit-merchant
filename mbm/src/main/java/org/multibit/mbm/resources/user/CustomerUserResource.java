package org.multibit.mbm.resources.user;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.user.CustomerUserBridge;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/user")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CustomerUserResource extends BaseResource {

  @GET
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveOwnProfile(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User user)
  {

    CustomerUserBridge bridge = new CustomerUserBridge(uriInfo, Optional.of(user));

    return ok(bridge, user);
  }

}