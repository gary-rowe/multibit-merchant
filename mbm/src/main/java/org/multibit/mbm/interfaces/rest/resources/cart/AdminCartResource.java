package org.multibit.mbm.interfaces.rest.resources.cart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.CartReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.interfaces.rest.api.cart.AdminUpdateCartDto;
import org.multibit.mbm.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.interfaces.rest.common.Representations;
import org.multibit.mbm.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.interfaces.rest.links.cart.CartLinks;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.domain.model.model.Cart} entities</li>
 * </ul>
 * <p>Note that a Cart has a 1:1 relationship with Customer and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 */
@Component
@Path(CartLinks.ADMIN_SELF_TEMPLATE)
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminCartResource extends BaseResource {

  @Resource(name = "hibernateCartDao")
  CartReadService cartDao;

  @Resource(name = "hibernateItemDao")
  ItemReadService itemReadService;

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

    PaginatedList<Cart> carts = cartDao.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = Representations.asPaginatedList(self(), "carts", carts, "/carts/{id}");

    return ok(representation);

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
    AdminUpdateCartDto updateCartRequest) {

    // Retrieve the cart
    Optional<Cart> cartOptional = cartDao.getById(cartId);
    ResourceAsserts.assertPresent(cartOptional,"cart");

    // Verify and apply any changes to the Cart
    Cart cart = cartOptional.get();
    apply(updateCartRequest,cart);

    // Persist the updated cart
    cart = cartDao.saveOrUpdate(cart);

    // Provide a representation to the client
    Representation representation = Representations.asDetail(self(), cart, Maps.<String, String>newHashMap());

    return ok(representation);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdateCartDto updateRequest, Cart entity) {

    for (PublicCartItemDto customerCartItem : updateRequest.getCartItems()) {
      Preconditions.checkNotNull(customerCartItem.getSKU(), "id");
      ResourceAsserts.assertPositive(customerCartItem.getQuantity(), "quantity");

      Optional<Item> item = itemReadService.getBySKU(customerCartItem.getSKU());
      ResourceAsserts.assertPresent(item,"item");

      entity.setItemQuantity(item.get(),customerCartItem.getQuantity());
    }
  }

  public void setCartDao(CartReadService cartDao) {
    this.cartDao = cartDao;
  }

  public void setItemReadService(ItemReadService itemReadService) {
    this.itemReadService = itemReadService;
  }
}