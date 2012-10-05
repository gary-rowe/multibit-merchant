package org.multibit.merchant.client.dojo.health;

/**
 * <p>HealthCheck to provide the following to application:</p>
 * <ul>
 * <li>Provision of checks against a given Configuration property </li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */

import com.yammer.metrics.core.HealthCheck;

public class DojoStoreHealthCheck extends HealthCheck {

  public DojoStoreHealthCheck() {
    super("Dojo Store");
  }

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}