package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.item.AdminCreateItemRequest;
import org.multibit.mbm.api.request.item.AdminUpdateItemRequest;
import org.multibit.mbm.api.response.hal.item.AdminItemBridge;
import org.multibit.mbm.api.response.hal.item.AdminItemCollectionBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.BaseResource;
import org.multibit.mbm.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.db.dto.Item} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin/item")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminItemResource extends BaseResource {

  @Resource(name = "hibernateItemDao")
  ItemDao itemDao;

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
    AdminCreateItemRequest createItemRequest) {

    // Build a item from the given request information
    Item item = ItemBuilder.newInstance()
      .withSKU(createItemRequest.getSKU())
      .build();

    // Perform basic verification
    Optional<Item> verificationItem = itemDao.getBySKU(item.getSKU());
    ResourceAsserts.assertNotConflicted(verificationItem,"item");

    // Persist the item
    Item persistentItem = itemDao.saveOrUpdate(item);

    // Provide a representation to the client
    AdminItemBridge bridge = new AdminItemBridge(uriInfo, Optional.of(adminUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentItem.getId().toString()).build();

    return created(bridge, persistentItem, location);

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
    @QueryParam("pageSize") Optional<String> rawPageSize,
    @QueryParam("pageNumber") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    List<Item> items = itemDao.getAllByPage(pageSize, pageNumber);

    // Provide a representation to the client
    AdminItemCollectionBridge bridge = new AdminItemCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, items);

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
    AdminUpdateItemRequest updateItemRequest) {

    // Retrieve the item
    Optional<Item> item = itemDao.getById(itemId);
    ResourceAsserts.assertPresent(item, "item");

    // Verify and apply any changes to the Item
    // TODO Fill in all details and provide general null safe field checking
    Item persistentItem = item.get();
    persistentItem.setSKU(updateItemRequest.getSKU());
    persistentItem.setGTIN(updateItemRequest.getGTIN());

    // Persist the updated item
    persistentItem = itemDao.saveOrUpdate(item.get());

    // Provide a representation to the client
    AdminItemBridge bridge = new AdminItemBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentItem);

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
    AdminDeleteEntityRequest deleteEntityRequest) {

    // Retrieve the item
    Optional<Item> item = itemDao.getById(itemId);
    ResourceAsserts.assertPresent(item,"item");

    // Verify and apply any changes to the Item
    Item persistentItem = item.get();
    persistentItem.setDeleted(true);
    persistentItem.setReasonForDelete(deleteEntityRequest.getReason());

    // Persist the updated item
    persistentItem = itemDao.saveOrUpdate(item.get());

    // Provide a representation to the client
    AdminItemBridge bridge = new AdminItemBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentItem);

  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}