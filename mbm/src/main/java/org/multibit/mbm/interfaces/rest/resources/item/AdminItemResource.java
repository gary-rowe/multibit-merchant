package org.multibit.mbm.interfaces.rest.resources.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.ItemBuilder;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.interfaces.rest.api.AdminDeleteEntityDto;
import org.multibit.mbm.interfaces.rest.api.item.AdminCreateItemDto;
import org.multibit.mbm.interfaces.rest.api.item.AdminUpdateItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.common.Representations;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.domain.model.model.Item} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin/item")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminItemResource extends BaseResource {

  @Resource(name = "hibernateItemDao")
  ItemReadService itemReadService;

  /**
   * Create a new Item from the given mandatory fields
   *
   * @param adminUser A User with administrator rights
   *
   * @return A response containing the minimum details of the created entity
   */
  @POST
  @Timed
  public Response create(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    AdminCreateItemDto createItemRequest) {

    // Build a item from the given request information
    Item item = ItemBuilder.newInstance()
      .withSKU(createItemRequest.getSKU())
      .build();

    // Perform basic verification
    Optional<Item> verificationItem = itemReadService.getBySKU(item.getSKU());
    ResourceAsserts.assertNotConflicted(verificationItem, "item");

    // Persist the item
    Item persistentItem = itemReadService.saveOrUpdate(item);

    // Provide a representation to the client
    Representation representation = Representations.asDetail(self(), persistentItem);
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentItem.getId().toString()).build();

    return created(representation, location);

  }

  /**
   * Provide a paged response of all items in the system
   *
   * @param adminUser     A User with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all items
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

    PaginatedList<Item> items = itemReadService.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = Representations.asPaginatedList(self(), items, "items/{id}");

    return ok(representation);

  }

  /**
   * Update an existing Item with the populated fields
   *
   * @param adminUser A item with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/{itemId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("itemId") Long itemId,
    AdminUpdateItemDto updateItemRequest) {

    // Retrieve the item
    Optional<Item> item = itemReadService.getById(itemId);
    ResourceAsserts.assertPresent(item, "item");

    // Verify and apply any changes to the Item
    // TODO Fill in all details and provide general null safe field checking
    Item persistentItem = item.get();
    persistentItem.setSKU(updateItemRequest.getSKU());
    persistentItem.setGTIN(updateItemRequest.getGTIN());

    // Persist the updated item
    persistentItem = itemReadService.saveOrUpdate(item.get());

    // Provide a representation to the client
    Representation representation = Representations.asDetail(self(), persistentItem);

    return ok(representation);

  }

  /**
   * Delete an existing Item (usually meaning set flag to deleted)
   *
   * @param adminUser A User with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @DELETE
  @Timed
  @Path("/{itemId}")
  public Response delete(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("itemId") Long itemId,
    AdminDeleteEntityDto deleteEntityRequest) {

    // Retrieve the item
    Optional<Item> item = itemReadService.getById(itemId);
    ResourceAsserts.assertPresent(item, "item");

    // Verify and apply any changes to the Item
    Item persistentItem = item.get();
    persistentItem.setDeleted(true);
    persistentItem.setReasonForDelete(deleteEntityRequest.getReason());

    // Persist the updated item
    persistentItem = itemReadService.saveOrUpdate(item.get());

    // Provide a representation to the client
    Representation representation = Representations.asDetail(self(), persistentItem);

    return ok(representation);

  }

  public void setItemReadService(ItemReadService itemReadService) {
    this.itemReadService = itemReadService;
  }
}