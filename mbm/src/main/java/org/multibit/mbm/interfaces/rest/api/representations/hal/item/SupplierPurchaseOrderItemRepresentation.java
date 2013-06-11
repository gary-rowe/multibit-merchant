package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.PurchaseOrderItem;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of a PurchaseOrderItem for a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierPurchaseOrderItemRepresentation {

  public Representation get(PurchaseOrderItem purchaseOrderItem) {

    Preconditions.checkNotNull(purchaseOrderItem, "purchaseOrderItem");
    Preconditions.checkNotNull(purchaseOrderItem.getItem().getId(), "id");

    // Create the Customer Item resource
    Representation publicItemRepresentation = new PublicItemRepresentation().get(purchaseOrderItem.getItem());

    // Create the wrapping PurchaseOrderItem resource
    RepresentationFactory factory = new DefaultRepresentationFactory();

    return factory.newRepresentation("/purchase-order/item/" + purchaseOrderItem.getItem().getSKU())
      .withProperty("supplier_sku", purchaseOrderItem.getSupplierSKU())
      .withProperty("supplier_gtin", purchaseOrderItem.getSupplierGTIN())
      .withProperty("batch_reference", purchaseOrderItem.getBatchReference())
      .withProperty("quantity", purchaseOrderItem.getQuantity())
      .withProperty("price_subtotal", purchaseOrderItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", purchaseOrderItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("purchase_order_item_subtotal", purchaseOrderItem.getPurchaseOrderItemSubtotal().getAmount().toPlainString())
      .withRepresentation("item", publicItemRepresentation);

  }

}
