package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.AdminUpdateCartRequest;
import org.multibit.mbm.api.request.cart.CustomerCartItem;
import org.multibit.mbm.api.response.hal.cart.AdminCartBridge;
import org.multibit.mbm.api.response.hal.cart.AdminCartCollectionBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.BaseResource;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.db.dto.Cart} entities</li>
 * </ul>
 * <p>Note that a Cart has a 1:1 relationship with Customer and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 */
@Path("/admin")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminCartResource extends BaseResource {

  CartDao cartDao;

  /**
   * Provide a paged response of all Carts in the system
   *
   * @param adminUser     A User with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all Carts
   */
  @GET
  @Timed
  @Path("/cart")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @QueryParam("pageSize") Optional<String> rawPageSize,
    @QueryParam("pageNumber") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    List<Cart> carts = cartDao.getAllByPage(pageSize, pageNumber);

    // Provide a representation to the client
    AdminCartCollectionBridge bridge = new AdminCartCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, carts);

  }

  /**
   * Update an existing Cart with the populated fields
   *
   * @param adminUser A cart with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/cart/{cartId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("cartId") Long cartId,
    AdminUpdateCartRequest updateCartRequest) {

    // Retrieve the cart
    Optional<Cart> cart = cartDao.getById(cartId);
    ResourceAsserts.assertPresent(cart,"cart");

    // Verify and apply any changes to the Cart
    Cart persistentCart = cart.get();

    for (CustomerCartItem cartItem : updateCartRequest.getCartItems()) {
      Long itemId = cartItem.getId();
      ResourceAsserts.assertPositive(itemId,"itemId");



    }


    // Persist the updated cart
    persistentCart = cartDao.saveOrUpdate(persistentCart);

    // Provide a representation to the client
    AdminCartBridge bridge = new AdminCartBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentCart);

  }

  public void setCartDao(CartDao cartDao) {
    this.cartDao = cartDao;
  }
}