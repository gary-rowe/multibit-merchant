package org.multibit.mbm.core.pricing;

import org.joda.money.BigMoney;

/**
 * <p>Interface to provide the following to {@link PriceBuilder}:</p>
 * <ul>
 * <li>Visitor pattern for pricing rule application</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public interface PricingRule {

  /**
   * @param entity An entity
   * @return True if this rule can be applied
   */
  boolean canProcess(Object entity);

  /**
   * @param price The current price requiring modification
   * @param entity The entity providing the basis for the modification
   * @return The modified price
   */
  BigMoney applyTo(BigMoney price, Object entity);

}
