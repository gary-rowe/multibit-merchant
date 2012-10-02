package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.DefaultCustomerBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.db.dto.Customer}:</p>
 * <ul>
 * <li>Provision of REST endpoints</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Path("/")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CustomerResource extends BaseResource {

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  @GET
  @Timed
  @Path("/customer")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveCustomer(@RestrictedTo({Authority.ROLE_CUSTOMER}) User user) {

    Customer customer = user.getCustomer();
    Assert.notNull(customer);

    DefaultCustomerBridge bridge = new DefaultCustomerBridge(uriInfo, Optional.of(user));

    return ok(bridge,customer);

  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

}