package org.multibit.mbm.client.item;

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

  public List<PublicItem> retrieveAllByPage() {
    URI uri = URI.create("http://localhost:8080/mbm/items?pn=0&ps=1");

    List list = jerseyClient
      .resource(uri)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString())
      .get(List.class);

    List<PublicItem> publicItems = Lists.newArrayList(list);

    return publicItems;
  }
}
