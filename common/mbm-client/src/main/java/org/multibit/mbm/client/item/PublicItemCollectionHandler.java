package org.multibit.mbm.client.item;

import com.google.common.collect.Lists;
import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.model.PublicItem;

import java.net.URI;
import java.util.List;

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

  public PublicItemCollectionHandler(JerseyClient jerseyClient) {
    this.jerseyClient = jerseyClient;
  }

  public List<PublicItem> retrieveAllByPage() {
    URI uri = URI.create("http://localhost:8080/mbm/items?pn=0&ps=1");
    List list= jerseyClient.get(uri, HalMediaType.APPLICATION_JSON_TYPE, List.class);

    List<PublicItem> publicItems = Lists.newArrayList(list);

    return publicItems;
  }
}
