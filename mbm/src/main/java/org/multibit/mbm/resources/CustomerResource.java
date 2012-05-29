package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.services.CustomerService;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

  @Resource(name="customerService")
  private CustomerService customerService=null;

  @GET
  @Timed
  @Path("/customer")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Customer findById(@QueryParam("openId") Optional<String> openId) {

    // TODO Consider link-driving with
    // UriBuilder.fromResource(CustomerResource.class).build();

    return customerService.findByOpenId(openId.get());
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}