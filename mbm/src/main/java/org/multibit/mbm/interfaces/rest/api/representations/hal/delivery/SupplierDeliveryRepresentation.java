package org.multibit.mbm.interfaces.rest.api.representations.hal.delivery;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.domain.model.model.Delivery;
import org.multibit.mbm.domain.model.model.DeliveryItem;
import org.multibit.mbm.interfaces.rest.api.representations.hal.item.SupplierDeliveryItemRepresentation;

/**
 * <p>Representation to provide the following to Suppliers:</p>
 * <ul>
 * <li>Creates {@link Representation} representations of a Delivery</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierDeliveryRepresentation {

  private final SupplierDeliveryItemRepresentation supplierDeliveryItemRepresentation = new SupplierDeliveryItemRepresentation();

  public Representation get(Delivery delivery) {
    Preconditions.checkNotNull(delivery, "delivery");
    Preconditions.checkNotNull(delivery.getId(), "id");

    // Do not reveal the ID to non-admins
    String basePath = "/delivery";

    // TODO Integrate with Preferences and DeliveryItem
    String currencySymbol = "Ƀ"; // or &pound; or &euro;
    String currencyCode = "BTC";

    // Calculate the value of the delivery items
    // TODO Allow for currency conversion
    BigMoney deliveryTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    BigMoney taxTotal = MoneyUtils.parseBitcoin("BTC 0.0000");
    for (DeliveryItem deliveryItem: delivery.getDeliveryItems()) {
      deliveryTotal = deliveryTotal.plus(deliveryItem.getPriceSubtotal());
      taxTotal = taxTotal.plus(deliveryItem.getTaxSubtotal());
    }

    // Create top-level resource
    Representation deliveryRepresentation = new DefaultRepresentationFactory()
      .newRepresentation(basePath)
      // Do not reveal the supplier to non-admins
      .withLink("supplier","/supplier")
      .withProperty("currency_symbol", currencySymbol)
      .withProperty("currency_code", currencyCode)
      .withProperty("price_total", deliveryTotal.getAmount().toPlainString())
      .withProperty("tax_total", taxTotal.getAmount().toPlainString())
      .withProperty("item_total", delivery.getItemTotal())
      .withProperty("quantity_total", delivery.getQuantityTotal())
      // End of build
      ;

    // Create sub-resources based on items
    for (DeliveryItem deliveryItem : delivery.getDeliveryItems()) {
      Representation publicDeliveryItemRepresentation = supplierDeliveryItemRepresentation.get(deliveryItem);
      deliveryRepresentation.withRepresentation("deliveryitems", publicDeliveryItemRepresentation);
    }

    return deliveryRepresentation;
  }

}
