package org.multibit.mbm.client;

import org.multibit.mbm.client.interfaces.rest.handlers.user.SupplierUserHandler;

import java.util.Locale;

/**
 * <p>RESTful client to provide the following to applications:</p>
 * <ul>
 * <li>Access to Supplier API for MultiBit Merchant</li>
 * </ul>
 * <p>This client provides access to protected resources for a supplier. For example, the supplier
 * can view their delivery history, purchase orders or edit their own details through this API.</p>
 *
 * @since 0.0.1
 *         
 */
public class SupplierMerchantClient extends BaseMerchantClient {

  /**
   * @param locale The client locale for appropriate representation
   */
  public SupplierMerchantClient(Locale locale) {
    super(locale);
  }

  /**
   * @param locale The client locale for appropriate representation
   */
  public static SupplierMerchantClient newInstance(Locale locale) {
    return new SupplierMerchantClient(locale);
  }

  /**
   * @return A suitable handler for single user requests by suppliers
   */
  public SupplierUserHandler user() {
    return new SupplierUserHandler(locale);
  }

}
