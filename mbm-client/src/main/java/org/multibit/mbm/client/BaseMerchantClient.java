package org.multibit.mbm.client;

import java.util.Locale;

/**
 * <p>Abstract base class to provide the following to merchant clients:</p>
 * <ul>
 * <li>Access to common methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseMerchantClient {

  protected final Locale locale;

  protected BaseMerchantClient(Locale locale) {
    this.locale = locale;
  }
}
