package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.DefaultUserBridge;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.SecurityService;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.db.dto.Customer}:</p>
 * <ul>
 * <li>Provision of REST endpoints</li>
 * </ul>
 *
 * @since 0.0.1
 * TODO Consider a base class with configured UriInfo and HttpHeaders (need to verify Prototype pattern)
 */
@Path("/")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class UserResource extends BaseResource<User> {

  @Resource(name="securityService")
  private SecurityService securityService =null;

  @GET
  @Timed
  @Path("/user")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response findById(@QueryParam("uuid") Optional<String> uuid) {

    User user =  securityService.getUserByUUID(uuid.get());

    DefaultUserBridge bridge = new DefaultUserBridge(uriInfo, Optional.of(user));

    return ok(bridge, user);

  }

  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }
}