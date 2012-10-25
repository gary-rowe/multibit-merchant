package org.multibit.mbm.client.handlers.item;

import com.google.common.collect.Lists;
import com.theoryinpractise.halbuilder.spi.Link;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.client.HalHmacResourceFactory;
import org.multibit.mbm.client.handlers.BaseHandler;
import org.multibit.mbm.model.ClientItem;

import java.util.List;
import java.util.Locale;

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
   * @return A list of {@link org.multibit.mbm.model.ClientItem}
   */
  public List<ClientItem> retrievePromotionalItemsByPage(int pageNumber, int pageSize) {

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
    ReadableResource rr = unmarshalHal(hal);

    // Extract the list of items
    List<ClientItem> clientItems = Lists.newArrayList();
    for (Resource itemResource : rr.getResources()) {

      List<Link> links = itemResource.getLinks();

      ClientItem clientItem = ClientItemHandler.buildClientItem(itemResource.getProperties(), links);

      clientItems.add(clientItem);
    }

    return clientItems;
  }
}
