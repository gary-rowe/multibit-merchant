package org.multibit.mbm.interfaces.rest.resources.cart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.CartReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.interfaces.rest.api.cart.PublicUpdateCartDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.interfaces.rest.common.Representations;
import org.multibit.mbm.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.interfaces.rest.links.cart.CartLinks;
import org.multibit.mbm.interfaces.rest.representations.CartRepresentations;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.domain.model.model.Cart}:</p>
 * <ul>
 * <li>Provision of REST endpoints for public interaction with their Cart</li>
 * </ul>
 * <p>Note that a Cart has a 1:1 relationship with Customer and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 *         
 */
@Component
@Path(CartLinks.PUBLIC_SELF_TEMPLATE)
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class PublicCartResource extends BaseResource {

  @Resource(name="hibernateCartDao")
  CartReadService cartDao;

  @Resource(name="hibernateItemDao")
  ItemReadService itemReadService;

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
    Preconditions.checkNotNull(publicUser.getCustomer(), "customer");

    Cart cart = publicUser.getCustomer().getCart();

    Representation representation = CartRepresentations.retrieveOwnCart(cart);

    return ok(representation);

  }

  /**
   * Update an existing Cart with the populated fields
   *
   * @param publicUser A cart with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  public Response update(
    @RestrictedTo({Authority.ROLE_PUBLIC})
    User publicUser,
    PublicUpdateCartDto updateCartRequest) {

    // Retrieve the cart
    Cart cart = publicUser.getCustomer().getCart();

    // Verify and apply any changes to the Cart
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
  private void apply(PublicUpdateCartDto updateRequest, Cart entity) {

    for (PublicCartItemDto customerCartItem : updateRequest.getCartItems()) {
      Preconditions.checkNotNull(customerCartItem.getSKU(), "sku");
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