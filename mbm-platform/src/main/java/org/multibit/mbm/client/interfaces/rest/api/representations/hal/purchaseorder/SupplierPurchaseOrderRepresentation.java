package org.multibit.mbm.client.interfaces.rest.api.representations.hal.purchaseorder;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.client.domain.model.model.PurchaseOrder;
import org.multibit.mbm.client.domain.model.model.PurchaseOrderItem;
import org.multibit.mbm.client.interfaces.rest.api.representations.hal.item.SupplierPurchaseOrderItemRepresentation;

/**
 * <p>Representation to provide the following to Suppliers:</p>
 * <ul>
 * <li>Creates {@link Representation} representations of a PurchaseOrder</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierPurchaseOrderRepresentation {

  private final SupplierPurchaseOrderItemRepresentation supplierPurchaseOrderItemRepresentation = new SupplierPurchaseOrderItemRepresentation();

  public Representation get(PurchaseOrder purchaseOrder) {
    Preconditions.checkNotNull(purchaseOrder, "purchaseOrder");
    Preconditions.checkNotNull(purchaseOrder.getId(), "id");

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
    Representation purchaseOrderRepresentation = new DefaultRepresentationFactory()
      .newRepresentation(basePath)
      // Do not reveal the supplier to non-admins
      .withLink("supplier","/supplier")
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
      Representation publicPurchaseOrderItemRepresentation = supplierPurchaseOrderItemRepresentation.get(purchaseOrderItem);
      purchaseOrderRepresentation.withRepresentation("purchaseOrderItems", publicPurchaseOrderItemRepresentation);
    }

    return purchaseOrderRepresentation;
  }

}
