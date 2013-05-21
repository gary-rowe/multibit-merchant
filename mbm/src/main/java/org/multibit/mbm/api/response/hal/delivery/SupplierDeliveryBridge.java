package org.multibit.mbm.api.response.hal.delivery;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.api.response.hal.item.SupplierDeliveryItemBridge;
import org.multibit.mbm.core.model.Delivery;
import org.multibit.mbm.core.model.DeliveryItem;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to Suppliers:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations of a Delivery</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierDeliveryBridge extends BaseBridge<Delivery> {

  private final SupplierDeliveryItemBridge supplierDeliveryItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.core.model.User} to provide a security principal
   */
  public SupplierDeliveryBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    supplierDeliveryItemBridge = new SupplierDeliveryItemBridge(uriInfo, principal);
  }

  public Resource toResource(Delivery delivery) {
    ResourceAsserts.assertNotNull(delivery, "delivery");
    ResourceAsserts.assertNotNull(delivery.getId(),"id");

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
    Resource deliveryResource = getResourceFactory()
      .newResource(basePath)
      // Do not reveal the supplier to non-admins
      .withLink("/supplier", "supplier")
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
      Resource publicDeliveryItemResource = supplierDeliveryItemBridge.toResource(deliveryItem);
      deliveryResource.withSubresource("deliveryitems", publicDeliveryItemResource);
    }

    return deliveryResource;
  }

}
