package org.multibit.mbm.resources.cart;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.PublicCartItem;
import org.multibit.mbm.api.request.cart.PublicUpdateCartRequest;
import org.multibit.mbm.api.response.hal.cart.PublicCartBridge;
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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Component
@Path("/cart")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class PublicCartResource extends BaseResource {

  CartDao cartDao;
  ItemDao itemDao;

  /**
   * Provides this Customer's Cart
   *
   * @param publicUser A public User
   *
   * @return A response containing the Customer Cart
   */
  @GET
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveOwnCart(
    @RestrictedTo({Authority.ROLE_PUBLIC})
    User publicUser) {

    // Validation
    ResourceAsserts.assertNotNull(publicUser.getCustomer(), "customer");

    Cart cart = publicUser.getCustomer().getCart();

    // Provide a representation to the client
    PublicCartBridge bridge = new PublicCartBridge(uriInfo, Optional.of(publicUser));

    return ok(bridge, cart);

  }

  /**
   * Update an existing Cart with the populated fields
   *
   * @param customerUser A cart with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  public Response update(
    @RestrictedTo({Authority.ROLE_PUBLIC})
    User customerUser,
    PublicUpdateCartRequest updateCartRequest) {

    // Retrieve the cart
    Cart cart = customerUser.getCustomer().getCart();

    // Verify and apply any changes to the Cart
    apply(updateCartRequest,cart);

    // Persist the updated cart
    cart = cartDao.saveOrUpdate(cart);

    // Provide a representation to the client
    PublicCartBridge bridge = new PublicCartBridge(uriInfo, Optional.of(customerUser));

    return ok(bridge, cart);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(PublicUpdateCartRequest updateRequest, Cart entity) {

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