package org.multibit.mbm.client;

import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.client.item.PublicItemCollectionHandler;

/**
 * <p>RESTful client to provide the following to applications:</p>
 * <ul>
 * <li>Access to public API for the MultiBit Merchant</li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 *   TODO Add in some examples for different use cases
 * </pre>
 *
 * @since 0.0.1
 *         
 */
public class PublicMerchantClient {

  private final JerseyClient jerseyClient;

  /**
   * @param jerseyClient The {@link JerseyClient} to use
   */
  private PublicMerchantClient(JerseyClient jerseyClient) {
    this.jerseyClient = jerseyClient;
  }

  /**
   * @param jerseyClient A {@link JerseyClient} to provide connectivity with metrics
   * @return A new instance of the client
   */
  public static PublicMerchantClient newInstance(JerseyClient jerseyClient) {
    return new PublicMerchantClient(jerseyClient);
  }

  /**
   * @return A suitable handler for item collections
   */
  public PublicItemCollectionHandler items() {
    return new PublicItemCollectionHandler(jerseyClient);
  }
}
