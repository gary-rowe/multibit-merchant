package org.multibit.mbm.resources.cart;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.AdminUpdateCartRequest;
import org.multibit.mbm.api.request.cart.PublicCartItem;
import org.multibit.mbm.api.response.hal.cart.AdminCartBridge;
import org.multibit.mbm.api.response.hal.cart.AdminCartCollectionBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.BaseResource;
import org.multibit.mbm.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Component
@Path("/admin/carts")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminCartResource extends BaseResource {

  @Resource(name = "hibernateCartDao")
  CartDao cartDao;

  @Resource(name = "hibernateItemDao")
  ItemDao itemDao;

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
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @QueryParam("ps") Optional<String> rawPageSize,
    @QueryParam("pn") Optional<String> rawPageNumber) {

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
  @Path("/{cartId}")
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
    apply(updateCartRequest,persistentCart);

    // Persist the updated cart
    persistentCart = cartDao.saveOrUpdate(persistentCart);

    // Provide a representation to the client
    AdminCartBridge bridge = new AdminCartBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentCart);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdateCartRequest updateRequest, Cart entity) {

    for (PublicCartItem customerCartItem : updateRequest.getCartItems()) {
      ResourceAsserts.assertNotNull(customerCartItem.getId(), "id");
      ResourceAsserts.assertPositive(customerCartItem.getQuantity(), "quantity");

      Optional<Item> item = itemDao.getById(customerCartItem.getId());
      ResourceAsserts.assertPresent(item,"item");

      entity.setItemQuantity(item.get(),customerCartItem.getQuantity());
    }
  }

  public void setCartDao(CartDao cartDao) {
    this.cartDao = cartDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}