package org.multibit.mbm.client;

import org.multibit.mbm.client.handlers.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.handlers.item.PublicItemHandler;

import java.util.Locale;

/**
 * <p>RESTful client to provide the following to applications:</p>
 * <ul>
 * <li>Access to public API for the MultiBit Merchant</li>
 * </ul>
 * <p>This client provides access to public resources for an anonymous visitor. For example, the visitor
 * can search and view items, or manage their shopping cart through this API.</p>
 *
 * @since 0.0.1
 *         
 */
public class PublicMerchantClient extends BaseMerchantClient {

  protected PublicMerchantClient(Locale locale) {
    super(locale);
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
