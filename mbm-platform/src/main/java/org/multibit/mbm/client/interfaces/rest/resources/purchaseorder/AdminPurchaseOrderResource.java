package org.multibit.mbm.client.interfaces.rest.resources.purchaseorder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.domain.model.model.PurchaseOrder;
import org.multibit.mbm.client.domain.model.model.User;
import org.multibit.mbm.client.domain.repositories.ItemReadService;
import org.multibit.mbm.client.domain.repositories.PurchaseOrderReadService;
import org.multibit.mbm.client.interfaces.rest.api.cart.purchaseorder.AdminUpdatePurchaseOrderRequest;
import org.multibit.mbm.client.interfaces.rest.api.cart.purchaseorder.BuyerPurchaseOrderItem;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.interfaces.rest.api.representations.hal.purchaseorder.AdminPurchaseOrderCollectionRepresentation;
import org.multibit.mbm.client.interfaces.rest.api.representations.hal.purchaseorder.AdminPurchaseOrderRepresentation;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.multibit.mbm.client.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.client.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.client.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.client.domain.model.model.PurchaseOrder} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin/purchase-orders")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminPurchaseOrderResource extends BaseResource {

  @Resource(name = "hibernatePurchaseOrderDao")
  PurchaseOrderReadService purchaseOrderReadService;

  @Resource(name = "hibernateItemDao")
  ItemReadService itemReadService;

  /**
   * Provide a paged response of all PurchaseOrders in the system
   *
   * @param buyerUser     A User with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all PurchaseOrders
   */
  @GET
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_BUYER})
    User buyerUser,
    @QueryParam("ps") Optional<String> rawPageSize,
    @QueryParam("pn") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    PaginatedList<PurchaseOrder> purchaseOrders = purchaseOrderReadService.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = new AdminPurchaseOrderCollectionRepresentation().get(purchaseOrders);

    return ok(representation);

  }

  /**
   * Update an existing PurchaseOrder with the populated fields
   *
   * @param adminUser A purchaseOrder with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/{purchaseOrderId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_BUYER})
    User adminUser,
    @PathParam("purchaseOrderId") Long purchaseOrderId,
    AdminUpdatePurchaseOrderRequest updatePurchaseOrderRequest) {

    // Retrieve the purchaseOrder
    Optional<PurchaseOrder> purchaseOrder = purchaseOrderReadService.getById(purchaseOrderId);
    ResourceAsserts.assertPresent(purchaseOrder,"purchaseOrder");

    // Verify and apply any changes to the PurchaseOrder
    PurchaseOrder persistentPurchaseOrder = purchaseOrder.get();
    apply(updatePurchaseOrderRequest,persistentPurchaseOrder);

    // Persist the updated purchaseOrder
    persistentPurchaseOrder = purchaseOrderReadService.saveOrUpdate(persistentPurchaseOrder);

    // Provide a representation to the client
    Representation representation = new AdminPurchaseOrderRepresentation().get(persistentPurchaseOrder);

    return ok(representation);

  }

  /**
   * TODO Refactor into a common handler
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(AdminUpdatePurchaseOrderRequest updateRequest, PurchaseOrder entity) {

    for (BuyerPurchaseOrderItem supplierPurchaseOrderItem : updateRequest.getPurchaseOrderItems()) {
      Preconditions.checkNotNull(supplierPurchaseOrderItem.getSKU(), "id");
      ResourceAsserts.assertPositive(supplierPurchaseOrderItem.getQuantity(), "quantity");

      Optional<Item> item = itemReadService.getBySKU(supplierPurchaseOrderItem.getSKU());
      ResourceAsserts.assertPresent(item,"item");

      entity.setItemQuantity(item.get(),supplierPurchaseOrderItem.getQuantity());
    }
  }

  public void setPurchaseOrderReadService(PurchaseOrderReadService purchaseOrderReadService) {
    this.purchaseOrderReadService = purchaseOrderReadService;
  }

  public void setItemReadService(ItemReadService itemReadService) {
    this.itemReadService = itemReadService;
  }
}