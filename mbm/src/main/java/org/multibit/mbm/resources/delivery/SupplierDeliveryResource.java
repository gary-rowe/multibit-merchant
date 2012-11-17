package org.multibit.mbm.resources.delivery;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.delivery.SupplierDeliveryItem;
import org.multibit.mbm.api.request.delivery.SupplierUpdateDeliveryRequest;
import org.multibit.mbm.api.response.hal.delivery.SupplierDeliveryBridge;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.core.model.Delivery;
import org.multibit.mbm.core.model.Item;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.BaseResource;
import org.multibit.mbm.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.core.model.Delivery}:</p>
 * <ul>
 * <li>Provision of REST endpoints for supplier interaction with their Delivery</li>
 * </ul>
 * <p>Note that a Delivery has a 1:1 relationship with Supplier and is meaningless
 * without it. Therefore there is no "Create" or "Delete" requirement.</p>
 *
 * @since 0.0.1
 *         
 */
@Component
@Path("/supplier/delivery")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class SupplierDeliveryResource extends BaseResource {

  @Resource(name = "hibernateDeliveryDao")
  DeliveryDao deliveryDao;

  @Resource(name = "hibernateItemDao")
  ItemDao itemDao;

  /**
   * Provides this Supplier's Delivery
   *
   * @param supplierUser A supplier User
   *
   * @return A response containing the Supplier Delivery
   */
  @GET
  @Path("/{supplier_reference}")
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveOwnDelivery(
    @RestrictedTo({Authority.ROLE_SUPPLIER})
    User supplierUser,
    @PathParam("supplier_reference")
    String supplierReference
  ) {

    // Validation
    ResourceAsserts.assertNotNull(supplierUser.getSupplier(), "supplier");

    if (supplierUser.getSupplier().getDeliveries().isEmpty()) {
      // TODO Fill in the location of a Delivery by a Supplier
    }
    Delivery delivery = supplierUser.getSupplier().getDeliveries().iterator().next();

    // Provide a representation to the client
    SupplierDeliveryBridge bridge = new SupplierDeliveryBridge(uriInfo, Optional.of(supplierUser));

    return ok(bridge, delivery);

  }

  /**
   * Update an existing Delivery with the populated fields
   *
   * @param supplierUser A Supplier with update rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  public Response update(
    @RestrictedTo({Authority.ROLE_SUPPLIER})
    User supplierUser,
    SupplierUpdateDeliveryRequest updateDeliveryRequest) {

    // Retrieve the delivery
    Delivery delivery = supplierUser.getSupplier().getDeliveries().iterator().next();

    // Verify and apply any changes to the Delivery
    apply(updateDeliveryRequest, delivery);

    // Persist the updated delivery
    delivery = deliveryDao.saveOrUpdate(delivery);

    // Provide a representation to the client
    SupplierDeliveryBridge bridge = new SupplierDeliveryBridge(uriInfo, Optional.of(supplierUser));

    return ok(bridge, delivery);

  }

  /**
   * TODO Refactor into a common handler
   *
   * @param updateRequest The update request containing the changes
   * @param entity        The entity to which these changes will be applied
   */
  private void apply(SupplierUpdateDeliveryRequest updateRequest, Delivery entity) {

    for (SupplierDeliveryItem supplierDeliveryItem : updateRequest.getDeliveryItems()) {
      ResourceAsserts.assertNotNull(supplierDeliveryItem.getSKU(), "sku");
      ResourceAsserts.assertPositive(supplierDeliveryItem.getQuantity(), "quantity");

      Optional<Item> item = itemDao.getBySKU(supplierDeliveryItem.getSKU());
      ResourceAsserts.assertPresent(item, "item");

      entity.setItemQuantity(item.get(), supplierDeliveryItem.getQuantity());
    }
  }

  public void setDeliveryDao(DeliveryDao deliveryDao) {
    this.deliveryDao = deliveryDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}