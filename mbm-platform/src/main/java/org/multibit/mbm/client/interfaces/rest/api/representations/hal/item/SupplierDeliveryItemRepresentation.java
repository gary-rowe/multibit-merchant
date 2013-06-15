package org.multibit.mbm.client.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.client.domain.model.model.DeliveryItem;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.client.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of a DeliveryItem for a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierDeliveryItemRepresentation {

  public Representation get(DeliveryItem deliveryItem) {

    Preconditions.checkNotNull(deliveryItem, "deliveryItem");
    Preconditions.checkNotNull(deliveryItem.getItem().getId(), "id");

    // Create the Customer Item resource
    Representation publicItemRepresentation = new PublicItemRepresentation().get(deliveryItem.getItem());

    // Create the wrapping DeliveryItem resource
    RepresentationFactory factory = new DefaultRepresentationFactory();

    return factory.newRepresentation("/delivery/item/" + deliveryItem.getItem().getSKU())
      .withProperty("supplier_sku", deliveryItem.getSupplierSKU())
      .withProperty("supplier_gtin", deliveryItem.getSupplierGTIN())
      .withProperty("batch_reference", deliveryItem.getBatchReference())
      .withProperty("quantity", deliveryItem.getQuantity())
      .withProperty("price_subtotal", deliveryItem.getPriceSubtotal().getAmount().toPlainString())
      .withProperty("tax_subtotal", deliveryItem.getTaxSubtotal().getAmount().toPlainString())
      .withProperty("delivery_item_subtotal", deliveryItem.getDeliveryItemSubtotal().getAmount().toPlainString())
      .withRepresentation("item", publicItemRepresentation);

  }

}
