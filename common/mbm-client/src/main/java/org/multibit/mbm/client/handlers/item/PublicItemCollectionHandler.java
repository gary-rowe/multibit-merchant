package org.multibit.mbm.client.handlers.item;

import com.google.common.collect.Lists;
import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.model.PublicItem;

import javax.ws.rs.core.HttpHeaders;
import java.net.URI;
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
public class PublicItemCollectionHandler {

  private final JerseyClient jerseyClient;
  private final Locale locale;

  /**
   * @param jerseyClient The {@link JerseyClient} for consuming the upstream data
   * @param locale       The locale that applies to the request
   */
  public PublicItemCollectionHandler(JerseyClient jerseyClient, Locale locale) {
    this.jerseyClient = jerseyClient;
    this.locale = locale;
  }

  /**
   *
   * Retrieve promotional items by page
   *
   * @param pageNumber The page number (e.g. 0 (first), 1, 2 etc)
   * @param pageSize The number of results per page
   * @return A list of {@link PublicItem}
   */
  public List<PublicItem> retrievePromotionalItemsByPage(int pageNumber, int pageSize) {

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
    String rawUri = String.format("http://localhost:8080/mbm/items?pn=%d&ps=%d"
      ,pageNumber, pageSize);
    URI uri = URI.create(rawUri);

    List list = jerseyClient
      .resource(uri)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString())
      .get(List.class);

    List<PublicItem> publicItems = Lists.newArrayList(list);

    return publicItems;
  }
}
