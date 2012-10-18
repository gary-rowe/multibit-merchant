package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.customer.CustomerBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.springframework.stereotype.Component;
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
@Component
@Path("/customer")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CustomerResource extends BaseResource {

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  @GET
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveCustomer(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User customerUser) {

    // Verify that the user has a customer
    Customer customer = customerUser.getCustomer();
    Assert.notNull(customer);

    CustomerBridge bridge = new CustomerBridge(uriInfo, Optional.of(customerUser));

    return ok(bridge,customer);

  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

}