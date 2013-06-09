package org.multibit.mbm.client.handlers.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.interfaces.rest.api.item.ItemDto;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of single item requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientItemHandler extends BaseHandler {

  /**
   * @param locale The locale providing i18n information
   */
  public ClientItemHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve single item using its ID
   *
   * @param sku The required Stock Keeping Unit (SKU)
   *
   * @return A matching {@link org.multibit.mbm.interfaces.rest.api.item.ItemDto}
   */
  public Optional<ItemDto> retrieveBySku(String sku) {

    // Sanity check
    // TODO How to sanity check an SKU?

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = "/items/" + sku;
    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .get(String.class);

    // Read the HAL
    ReadableRepresentation rr = unmarshalHal(hal);

    Map<String, Object> properties = rr.getProperties();
    List<Link> links = rr.getLinks();

    ItemDto item = buildClientItem(properties, links);

    return Optional.of(item);
  }

  /**
   * @param properties The HAL resource properties
   * @param links      The HAL links
   *
   * @return A PublicItem
   */
  public static ItemDto buildClientItem(Map<String, Object> properties, List<Link> links) {
    ItemDto item = new ItemDto();

    // Mandatory properties (will cause IllegalStateException if not present)
    item.setSKU(getMandatoryPropertyAsString("sku", properties));
    item.setPrice(getMandatoryPropertyAsString("price", properties));
    item.setTaxRate(getMandatoryPropertyAsString("tax_rate", properties));

    // Optional direct properties
    if (properties.containsKey("gtin")) {
      Object gtin = properties.get("gtin");
      if (gtin != null) {
        item.setGTIN((String) gtin);
      }
    }

    // Optional properties
    for (Map.Entry<String, Object> entry : properties.entrySet()) {
      if ("sku".equals(entry.getKey()) ||
        "gtin".equals(entry.getKey()) ||
        "price".equals(entry.getKey()) ||
        "tax_rate".equals(entry.getKey())) {
        continue;
      }
      item.getOptionalProperties().put(entry.getKey(), (String) entry.getValue());
    }

    // Optional links
    for (Link link : links) {
      item.getOptionalProperties().put(link.getRel(), link.getHref());
    }

    return item;
  }

}
