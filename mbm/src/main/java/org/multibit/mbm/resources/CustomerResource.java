package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.services.CustomerService;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.persistence.dto.Customer}:</p>
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
  public Customer findById(@QueryParam("openId") Optional<String> openId) {
    return customerService.findByOpenId(openId.get());
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}