package org.multibit.mbm.client;

import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.client.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.item.PublicItemHandler;

import java.util.Locale;

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
  private final Locale locale;

  /**
   * @param jerseyClient A {@link JerseyClient} to provide connectivity with metrics
   * @param locale The client locale for appropriate representation
   */
  private PublicMerchantClient(JerseyClient jerseyClient, Locale locale) {
    this.jerseyClient = jerseyClient;
    this.locale = locale;
  }

  /**
   * @param jerseyClient A {@link JerseyClient} to provide connectivity with metrics
   * @param locale The client locale for appropriate representation
   */
  public static PublicMerchantClient newInstance(JerseyClient jerseyClient, Locale locale) {
    return new PublicMerchantClient(jerseyClient,locale);
  }

  /**
   * @return A suitable handler for single item requests
   */
  public PublicItemHandler item() {
    return new PublicItemHandler(jerseyClient,locale);
  }

  /**
   * @return A suitable handler for item collection searches
   */
  public PublicItemCollectionHandler items() {
    return new PublicItemCollectionHandler(jerseyClient,locale);
  }

}
