package org.multibit.mbm.interfaces.rest.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.domain.model.model.PurchaseOrderItem;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of a PurchaseOrderItem for a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierPurchaseOrderItemBridge extends BaseBridge<PurchaseOrderItem> {

  private final PublicItemBridge publicItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public SupplierPurchaseOrderItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    publicItemBridge = new PublicItemBridge(uriInfo, principal);
  }

  public Resource toResource(PurchaseOrderItem purchaseOrderItem) {

    ResourceAsserts.assertNotNull(purchaseOrderItem, "purchaseOrderItem");
    ResourceAsserts.assertNotNull(purchaseOrderItem.getItem().getId(),"id");

    // Create the Customer Item resource
    Resource publicItemResource = publicItemBridge.toResource(purchaseOrderItem.getItem());

    // Create the wrapping PurchaseOrderItem resource
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/purchase-order/item/" + purchaseOrderItem.getItem().getSKU())
      .withProperty("supplier_sku", purchaseOrderItem.getSupplierSKU())
      .withProperty("supplier_gtin", purchaseOrderItem.getSupplierGTIN())
      .withProperty("batch_reference", purchaseOrderItem.getBatchReference())
      .withProperty("quantity", purchaseOrderItem.getQuantity())
      .withProperty("price_subtotal", purchaseOrderItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", purchaseOrderItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("purchase_order_item_subtotal", purchaseOrderItem.getPurchaseOrderItemSubtotal().getAmount().toPlainString())
      .withSubresource("item", publicItemResource);

  }

}
