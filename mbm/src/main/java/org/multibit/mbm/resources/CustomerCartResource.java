package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.response.hal.cart.CustomerCartBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.db.dto.Cart}:</p>
 * <ul>
 * <li>Provision of REST endpoints for Customer interaction with their Cart</li>
 * </ul>
 * <p>Note that a Cart has a 1:1 relationship with Customer and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 *         
 */
@Path("/")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CustomerCartResource extends BaseResource {

  /**
   * Provide a paged response of all Carts in the system
   *
   * @param customerUser A Customer User
   *
   * @return A response containing the Customer Cart
   */
  @GET
  @Timed
  @Path("/cart")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User customerUser) {

    // Validation
    ResourceAsserts.assertNotNull(customerUser.getCustomer(),"customer");

    Cart cart = customerUser.getCustomer().getCart();

    // Provide a representation to the client
    CustomerCartBridge bridge = new CustomerCartBridge(uriInfo, Optional.of(customerUser));

    return ok(bridge, cart);

  }

}