package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.CreateCartRequest;
import org.multibit.mbm.api.response.CartItemResponse;
import org.multibit.mbm.api.response.CartResponse;
import org.multibit.mbm.api.response.hal.DefaultCartResponseBridge;
import org.multibit.mbm.api.response.hal.DefaultCustomerBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CatalogService;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.db.dto.Cart}:</p>
 * <ul>
 * <li>Provision of REST endpoints</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Path("/")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class CartResource extends BaseResource<CartResponse> {

  private static final Logger log = LoggerFactory.getLogger(CartResource.class);

  @Resource(name = "catalogService")
  private CatalogService catalogService = null;

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  /**
   * Retrieves the  Cart for the authenticated User
   *
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
   *
   * @param createCartRequest The initial Cart contents
   * @return The response
   */
  @POST
  @Path("/cart")
  public Response createCart(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User user,
    CreateCartRequest createCartRequest,
    @Context UriInfo uriInfo) {

    // TODO Validate the request
    Customer customer = user.getCustomer();

    for (CartItemResponse cartItemSummary : createCartRequest.getCartItemSummaries()) {
      Long itemId = cartItemSummary.getId();
      int quantity = cartItemSummary.getQuantity();
      customer = customerService.setCartItemQuantity(customer, itemId, quantity);
    }

    CartResponse cartResponse = new CartResponse(customer.getCart());

    // Ensure the new Cart can be found using its ID
    URI location = uriInfo.getAbsolutePathBuilder().path("/cart/"+customer.getCart().getId()).build();

    DefaultCartResponseBridge bridge = new DefaultCartResponseBridge(uriInfo, Optional.of(customer.getUser()));

    // Provide the entire Cart as a shortcut for lazy clients
    return created(bridge, cartResponse, location);

  }

  public void setCatalogService(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}