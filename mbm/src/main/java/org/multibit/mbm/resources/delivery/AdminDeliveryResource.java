package org.multibit.mbm.resources.delivery;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.delivery.AdminUpdateDeliveryRequest;
import org.multibit.mbm.api.request.delivery.SupplierDeliveryItem;
import org.multibit.mbm.api.response.hal.delivery.AdminDeliveryBridge;
import org.multibit.mbm.api.response.hal.delivery.AdminDeliveryCollectionBridge;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Delivery;
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
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.db.dto.Delivery} entities</li>
 * </ul>
 * <p>Note that a Delivery has a 1:1 relationship with Supplier and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin/deliveries")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminDeliveryResource extends BaseResource {

  @Resource(name = "hibernateDeliveryDao")
  DeliveryDao deliveryDao;

  @Resource(name = "hibernateItemDao")
  ItemDao itemDao;

  /**
   * Provide a paged response of all Deliverys in the system
   *
   * @param adminUser     A User with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all Deliverys
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

    List<Delivery> deliveries = deliveryDao.getAllByPage(pageSize, pageNumber);

    // Provide a representation to the client
    AdminDeliveryCollectionBridge bridge = new AdminDeliveryCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, deliveries);

  }

  /**
   * Update an existing Delivery with the populated fields
   *
   * @param adminUser A delivery with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/{deliveryId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("deliveryId") Long deliveryId,
    AdminUpdateDeliveryRequest updateDeliveryRequest) {

    // Retrieve the delivery
    Optional<Delivery> delivery = deliveryDao.getById(deliveryId);
    ResourceAsserts.assertPresent(delivery,"delivery");

    // Verify and apply any changes to the Delivery
    Delivery persistentDelivery = delivery.get();
    apply(updateDeliveryRequest,persistentDelivery);

    // Persist the updated delivery
    persistentDelivery = deliveryDao.saveOrUpdate(persistentDelivery);

    // Provide a representation to the client
    AdminDeliveryBridge bridge = new AdminDeliveryBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentDelivery);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdateDeliveryRequest updateRequest, Delivery entity) {

    for (SupplierDeliveryItem supplierDeliveryItem : updateRequest.getDeliveryItems()) {
      ResourceAsserts.assertNotNull(supplierDeliveryItem.getSKU(), "id");
      ResourceAsserts.assertPositive(supplierDeliveryItem.getQuantity(), "quantity");

      Optional<Item> item = itemDao.getBySKU(supplierDeliveryItem.getSKU());
      ResourceAsserts.assertPresent(item,"item");

      entity.setItemQuantity(item.get(),supplierDeliveryItem.getQuantity());
    }
  }

  public void setDeliveryDao(DeliveryDao deliveryDao) {
    this.deliveryDao = deliveryDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}