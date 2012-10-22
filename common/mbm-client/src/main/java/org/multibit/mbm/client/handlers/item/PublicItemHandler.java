package org.multibit.mbm.client.handlers.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.model.PublicItem;

import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of public single item requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicItemHandler extends BaseHandler {

  /**
   * @param locale       The locale providing i18n information
   */
  public PublicItemHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve single item using its ID
   *
   * @param sku The required Stock Keeping Unit (SKU)
   *
   * @return A matching {@link org.multibit.mbm.model.PublicItem}
   */
  public Optional<PublicItem> retrieveBySku(String sku) {

    // Sanity check
    // TODO How to sanity check an SKU?

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = "/items/"+ sku;
    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .get(String.class);

    // Read the HAL
    ReadableResource rr = readHalRepresentation(hal);

    Map<String, Optional<Object>> properties = rr.getProperties();

    PublicItem item = new PublicItem();
    // Mandatory properties (will cause IllegalStateException if not present)
    item.setSKU((String) properties.get("sku").get());
    // Optional direct properties
    if (properties.containsKey("gtin")) {
      Optional<Object> gtin = properties.get("gtin");
      if (gtin.isPresent()) {
        item.setGTIN((String) gtin.get());
      }
    }
    // Optional properties
    for (Map.Entry<String,Optional<Object>> entry: properties.entrySet()) {
      item.getOptionalProperties().put(entry.getKey(), (String) entry.getValue().get());
    }

    return Optional.of(item);
  }

}
