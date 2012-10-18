package org.multibit.mbm.client;

import org.multibit.mbm.client.handlers.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.handlers.item.PublicItemHandler;

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

  private final Locale locale;

  /**
   * @param locale The client locale for appropriate representation
   */
  public PublicMerchantClient(Locale locale) {
    this.locale=locale;
  }

  /**
   * @param locale The client locale for appropriate representation
   */
  public static PublicMerchantClient newInstance(Locale locale) {
    return new PublicMerchantClient(locale);
  }

  /**
   * @return A suitable handler for single item requests
   */
  public PublicItemHandler item() {
    return new PublicItemHandler(locale);
  }

  /**
   * @return A suitable handler for item collection searches
   */
  public PublicItemCollectionHandler items() {
    return new PublicItemCollectionHandler(locale);
  }

}
