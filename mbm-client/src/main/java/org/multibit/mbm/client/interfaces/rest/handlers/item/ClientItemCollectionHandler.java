package org.multibit.mbm.client.interfaces.rest.handlers.item;

import com.google.common.collect.Lists;
import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.interfaces.rest.handlers.BaseHandler;
import org.multibit.mbm.client.interfaces.rest.api.item.ItemDto;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of public item collection requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientItemCollectionHandler extends BaseHandler {

  /**
   * @param locale The locale providing i18n information
   */
  public ClientItemCollectionHandler(Locale locale) {
    super(locale);
  }

  /**
   * Retrieve promotional items by page
   *
   * @param pageNumber The page number (e.g. 0 (first), 1, 2 etc)
   * @param pageSize   The number of results per page
   *
   * @return A list of {@link org.multibit.mbm.client.interfaces.rest.api.item.ItemDto}
   */
  public List<ItemDto> retrievePromotionalItemsByPage(int pageNumber, int pageSize) {

    // Sanity check
    // TODO Consider Guava ranges?
    if (pageNumber < 0) {
      pageNumber = 0;
    }
    if (pageSize < 0) {
      pageSize = 0;
    }
    if (pageSize > 50) {
      pageSize = 50;
    }

    // TODO Replace "magic string" with auto-discover based on link rel
    String path = String.format("/items/promotion?pn=%d&ps=%d", pageNumber, pageSize);

    String hal = HalHmacResourceFactory.INSTANCE
      .newClientResource(locale, path)
      .get(String.class);

    // Read the HAL
    ReadableRepresentation rr = unmarshalHal(hal);

    // Extract the list of items
    List<ItemDto> clientItems = Lists.newArrayList();
    for (Map.Entry<String, ReadableRepresentation> itemResource : rr.getResources()) {

      List<Link> links = itemResource.getValue().getLinks();

      ItemDto clientItem = ClientItemHandler.buildClientItem(itemResource.getValue().getProperties(), links);

      clientItems.add(clientItem);
    }

    return clientItems;
  }
}
