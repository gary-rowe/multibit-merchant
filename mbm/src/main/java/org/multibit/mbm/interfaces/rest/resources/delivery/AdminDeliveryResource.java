package org.multibit.mbm.interfaces.rest.resources.delivery;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.DeliveryReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.interfaces.rest.api.delivery.AdminUpdateDeliveryDto;
import org.multibit.mbm.interfaces.rest.api.delivery.SupplierDeliveryItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.representations.hal.delivery.AdminDeliveryCollectionRepresentation;
import org.multibit.mbm.interfaces.rest.api.representations.hal.delivery.AdminDeliveryRepresentation;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.domain.model.model.Delivery} entities</li>
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
  DeliveryReadService deliveryReadService;

  @Resource(name = "hibernateItemDao")
  ItemReadService itemReadService;

  /**
   * Provide a paged response of all Deliveries in the system
   *
   * @param adminUser     A User with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all Deliveries
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

    PaginatedList<Delivery> deliveries = deliveryReadService.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = new AdminDeliveryCollectionRepresentation().get(deliveries);

    return ok(representation);

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
    AdminUpdateDeliveryDto updateDeliveryRequest) {

    // Retrieve the delivery
    Optional<Delivery> delivery = deliveryReadService.getById(deliveryId);
    ResourceAsserts.assertPresent(delivery,"delivery");

    // Verify and apply any changes to the Delivery
    Delivery persistentDelivery = delivery.get();
    apply(updateDeliveryRequest,persistentDelivery);

    // Persist the updated delivery
    persistentDelivery = deliveryReadService.saveOrUpdate(persistentDelivery);

    // Provide a representation to the client
    Representation representation = new AdminDeliveryRepresentation().get(persistentDelivery);

    return ok(representation);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdateDeliveryDto updateRequest, Delivery entity) {

    for (SupplierDeliveryItemDto supplierDeliveryItem : updateRequest.getDeliveryItems()) {
      Preconditions.checkNotNull(supplierDeliveryItem.getSKU(), "id");
      ResourceAsserts.assertPositive(supplierDeliveryItem.getQuantity(), "quantity");

      Optional<Item> item = itemReadService.getBySKU(supplierDeliveryItem.getSKU());
      ResourceAsserts.assertPresent(item,"item");

      entity.setItemQuantity(item.get(),supplierDeliveryItem.getQuantity());
    }
  }

  public void setDeliveryReadService(DeliveryReadService deliveryReadService) {
    this.deliveryReadService = deliveryReadService;
  }

  public void setItemReadService(ItemReadService itemReadService) {
    this.itemReadService = itemReadService;
  }
}