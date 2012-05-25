package org.multibit.mbm.resources;

import com.yammer.dropwizard.auth.Auth;
import org.multibit.mbm.services.CatalogService;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.rest.v1.client.cart.CartItemSummary;
import org.multibit.mbm.rest.v1.client.cart.CartResponse;
import org.multibit.mbm.rest.v1.client.cart.CreateCartRequest;
import org.multibit.mbm.persistence.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.persistence.dto.Cart}:</p>
 * <ul>
 * <li>Provision of REST endpoints</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {

  private static final Logger log = LoggerFactory.getLogger(CartResource.class);

  @Resource(name = "catalogService")
  private CatalogService catalogService = null;

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  /**
   * Retrieves the  Cart for the authenticated User
   * @return The response
   */
  @GET
  @Path("/cart")
  public CartResponse retrieveCart(@Auth User user) {

    // Validate the expected form of the data
    Assert.notNull(user.getCustomer());

    return new CartResponse(user.getCustomer().getCart());
  }

  /**
   * Creates a new Cart for the User
   * @param createCartRequest The initial Cart contents
   * @return The response
   */
  @POST
  @Path("/cart")
  public Response createCart(@Auth User user,
    CreateCartRequest createCartRequest,
    @Context UriInfo uriInfo) {

    // TODO Validate the request

    String sessionId = createCartRequest.getSessionId();

    Customer customer = user.getCustomer();

    for (CartItemSummary cartItemSummary: createCartRequest.getCartItemSummaries()) {
      Long itemId = cartItemSummary.getId();
      int quantity = cartItemSummary.getQuantity();
      customer = customerService.setCartItemQuantity(customer, itemId, quantity);
    }

    // Ensure the new Cart can be found (one Cart per User)
    URI location = uriInfo.getAbsolutePathBuilder().path("/cart").build();

    // Provide the entire Cart as a shortcut for lazy clients
    return Response.created(location).entity(new CartResponse(customer.getCart())).build();

  }

  public void setCatalogService(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}