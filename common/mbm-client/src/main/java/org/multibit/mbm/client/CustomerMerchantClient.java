package org.multibit.mbm.client;

import org.multibit.mbm.client.handlers.user.CustomerUserHandler;

import java.util.Locale;

/**
 * <p>RESTful client to provide the following to applications:</p>
 * <ul>
 * <li>Access to Customer API for MultiBit Merchant</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CustomerMerchantClient extends BaseMerchantClient {

  /**
   * @param locale The client locale for appropriate representation
   */
  public CustomerMerchantClient(Locale locale) {
    super(locale);
  }

  /**
   * @param locale The client locale for appropriate representation
   */
  public static CustomerMerchantClient newInstance(Locale locale) {
    return new CustomerMerchantClient(locale);
  }

  /**
   * @return A suitable handler for single user requests by customers
   */
  public CustomerUserHandler user() {
    return new CustomerUserHandler(locale);
  }

}
