package org.multibit.mbm.api.response.hal.purchaseorder;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.api.response.hal.item.SupplierPurchaseOrderItemBridge;
import org.multibit.mbm.core.model.PurchaseOrder;
import org.multibit.mbm.core.model.PurchaseOrderItem;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to Suppliers:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations of a PurchaseOrder</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierPurchaseOrderBridge extends BaseBridge<PurchaseOrder> {

  private final SupplierPurchaseOrderItemBridge supplierPurchaseOrderItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.core.model.User} to provide a security principal
   */
  public SupplierPurchaseOrderBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierPurchaseOrderItemBridge = new SupplierPurchaseOrderItemBridge(uriInfo, principal);
  }

  public Resource toResource(PurchaseOrder purchaseOrder) {
    ResourceAsserts.assertNotNull(purchaseOrder, "purchaseOrder");
    ResourceAsserts.assertNotNull(purchaseOrder.getId(),"id");

    // Do not reveal the ID to non-admins
    String basePath = "/purchaseOrder";

    // TODO Integrate with Presets and PurchaseOrderItem
    String currencySymbol = "Ƀ"; // or &pound; or &euro;
    String currencyCode = "BTC";

    // Calculate the value of the purchaseOrder items
    // TODO Allow for currency conversion
    BigMoney purchaseOrderTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    BigMoney taxTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    for (PurchaseOrderItem purchaseOrderItem: purchaseOrder.getPurchaseOrderItems()) {
      purchaseOrderTotal = purchaseOrderTotal.plus(purchaseOrderItem.getPriceSubtotal());
      taxTotal = taxTotal.plus(purchaseOrderItem.getTaxSubtotal());
    }

    // Create top-level resource
    Resource purchaseOrderResource = getResourceFactory()
      .newResource(basePath)
      // Do not reveal the supplier to non-admins
      .withLink("/supplier", "supplier")
      .withProperty("currency_symbol", currencySymbol)
      .withProperty("currency_code", currencyCode)
      .withProperty("price_total", purchaseOrderTotal.getAmount().toPlainString())
      .withProperty("tax_total", taxTotal.getAmount().toPlainString())
      .withProperty("item_total", purchaseOrder.getItemTotal())
      .withProperty("quantity_total", purchaseOrder.getQuantityTotal())
      // End of build
      ;

    // Create sub-resources based on items
    for (PurchaseOrderItem purchaseOrderItem : purchaseOrder.getPurchaseOrderItems()) {
      Resource publicPurchaseOrderItemResource = supplierPurchaseOrderItemBridge.toResource(purchaseOrderItem);
      purchaseOrderResource.withSubresource("purchaseOrderItems", publicPurchaseOrderItemResource);
    }

    return purchaseOrderResource;
  }

}
