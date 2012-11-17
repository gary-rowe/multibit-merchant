package org.multibit.mbm.core.pricing.rules;

import org.joda.money.BigMoney;
import org.multibit.mbm.core.model.PricingRule;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * <p>PricingRule to provide the following to the price builder:</p>
 * <ul>
 * <li>Modifies the given price by the given percentage through multiplication</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Entity
@DiscriminatorValue("P")
public class PercentagePricingRule extends PricingRule {

  @Column(name = "percentage", nullable = true)
  private double percentage = 0.0;

  /**
   * @return The percentage to apply to the price
   */
  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  @Override
  public BigMoney applyTo(BigMoney unitPrice) {
    return unitPrice.multipliedBy(percentage);
  }
}
