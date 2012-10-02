package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.CustomerCreateCartRequest;
import org.multibit.mbm.api.response.CustomerCartItem;
import org.multibit.mbm.api.response.CustomerCartResponse;
import org.multibit.mbm.api.response.hal.cart.CustomerCartBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.db.dto.Cart}:</p>
 * <ul>
 * <li>Provision of REST endpoints for Customer interaction with their Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Path("/")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CustomerCartResource extends BaseResource {

  private static final Logger log = LoggerFactory.getLogger(CustomerCartResource.class);

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  /**
   * Retrieves the  Cart for the authenticated User
   *
   * @return The response
   */
  @GET
  @Path("/cart")
  public CustomerCartResponse retrieveCart(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User user) {

    // Validate the expected form of the data
    Assert.notNull(user.getCustomer());

    return new CustomerCartResponse(user.getCustomer().getCart());
  }

  /**
   * Creates a new Cart for the User
   *
   * @param createCartRequest The initial Cart contents
   *
   * @return The response
   */
  @POST
  @Path("/cart")
  public Response createCart(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User user,
    CustomerCreateCartRequest createCartRequest,
    @Context UriInfo uriInfo) {

    // Validation
    Assert.notNull(user.getCustomer());

    // Locate the customer
    Customer customer = user.getCustomer();

    // Simply reflect the offered items back in the response for now
    for (CustomerCartItem cartItemSummary : createCartRequest.getCartItems()) {
      Long itemId = cartItemSummary.getId();
      int quantity = cartItemSummary.getQuantity();
      customer = customerService.setCartItemQuantity(customer, itemId, quantity);
    }

    CustomerCartResponse cartResponse = new CustomerCartResponse(customer.getCart());

    // Ensure the new Cart can be found using its ID
    URI location = uriInfo.getAbsolutePathBuilder().path(customer.getCart().getId().toString()).build();

    CustomerCartBridge bridge = new CustomerCartBridge(uriInfo, Optional.of(customer.getUser()));

    // Provide the entire Cart as a shortcut for lazy clients
    return created(bridge, cartResponse, location);

  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}