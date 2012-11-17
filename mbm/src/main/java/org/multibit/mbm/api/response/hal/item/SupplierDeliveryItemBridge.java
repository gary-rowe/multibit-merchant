package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.DeliveryItem;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Item}:</p>
 * <ul>
 * <li>Creates representations of a DeliveryItem for a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierDeliveryItemBridge extends BaseBridge<DeliveryItem> {

  private final PublicItemBridge publicItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public SupplierDeliveryItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    publicItemBridge = new PublicItemBridge(uriInfo, principal);
  }

  public Resource toResource(DeliveryItem deliveryItem) {

    ResourceAsserts.assertNotNull(deliveryItem, "deliveryItem");
    ResourceAsserts.assertNotNull(deliveryItem.getItem().getId(),"id");

    // Create the Customer Item resource
    Resource publicItemResource = publicItemBridge.toResource(deliveryItem.getItem());

    // Create the wrapping DeliveryItem resource
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/delivery/item/" + deliveryItem.getItem().getSKU())
      .withProperty("supplier_sku", deliveryItem.getSupplierSKU())
      .withProperty("supplier_gtin", deliveryItem.getSupplierGTIN())
      .withProperty("batch_reference", deliveryItem.getBatchReference())
      .withProperty("quantity", deliveryItem.getQuantity())
      .withProperty("price_subtotal", deliveryItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", deliveryItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("delivery_item_subtotal", deliveryItem.getDeliveryItemSubtotal().getAmount().toPlainString())
      .withSubresource("item", publicItemResource);

  }

}
