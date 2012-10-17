package org.multibit.mbm.client;

import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.client.handlers.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.handlers.item.PublicItemHandler;

import java.net.URI;
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
  private final URI mbmBaseUri;

  /**
   * @param locale       The locale providing i18n information
   * @param jerseyClient The client for retrieving upstream data
   * @param mbmBaseUri   The URI identifying the upstream server
   */
  public PublicMerchantClient(JerseyClient jerseyClient, Locale locale, URI mbmBaseUri) {
    this.jerseyClient= jerseyClient;
    this.locale=locale;
    this.mbmBaseUri=mbmBaseUri;
  }

  /**
   * @param jerseyClient A {@link JerseyClient} to provide connectivity with metrics
   * @param locale The client locale for appropriate representation
   * @param mbmBaseUri   The URI identifying the upstream server
   */
  public static PublicMerchantClient newInstance(JerseyClient jerseyClient, Locale locale, URI mbmBaseUri) {
    return new PublicMerchantClient(jerseyClient,locale,mbmBaseUri);
  }

  /**
   * @return A suitable handler for single item requests
   */
  public PublicItemHandler item() {
    return new PublicItemHandler(jerseyClient, locale, mbmBaseUri);
  }

  /**
   * @return A suitable handler for item collection searches
   */
  public PublicItemCollectionHandler items() {
    return new PublicItemCollectionHandler(jerseyClient,locale,mbmBaseUri);
  }

}
