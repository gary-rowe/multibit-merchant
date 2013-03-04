package org.multibit.mbm.core.pricing;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.joda.time.DateTime;
import org.multibit.mbm.core.model.Customer;
import org.multibit.mbm.core.model.PricingRule;
import org.multibit.mbm.core.model.Supplier;
import org.multibit.mbm.utils.DateUtils;

import java.util.List;

/**
 * <p>Business object to provide the following to {@link org.multibit.mbm.core.model.Item}:</p>
 * <ul>
 * <li>Calculation of price based on purchase order</li>
 * </ul>
 * <p>A price can only be determined as a result of the application of rules against
 * and original value.</p>
 *
 * @since 0.0.1
 *         
 */
public class PriceBuilder {

  private int quantity = 0;
  private DateTime start = DateUtils.nowUtc();
  private DateTime end = DateUtils.nowUtc();
  private BigMoney startingPrice = MoneyUtils.parse("GBP 0.0");
  private Optional<Customer> customer = Optional.absent();
  private Optional<Supplier> supplier = Optional.absent();

  private List<PricingRule> pricingRules = Lists.newArrayList();

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static PriceBuilder newInstance() {
    return new PriceBuilder();
  }

  public PriceBuilder() {
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public BigMoney build() {
    validateState();

    // Price is not a DTO
    BigMoney price = startingPrice;

    for (PricingRule pricingRule : pricingRules) {
      pricingRule.setCustomer(customer);
      pricingRule.setSupplier(supplier);
      pricingRule.setQuantity(quantity);
      // TODO Consider date ranges (use discriminator value e.g. R(A,B], R(o,o))
      // Test filtering rules
      if (pricingRule.skip()) {
        continue;
      }
      if (pricingRule.halt()) {
        break;
      }
      price = pricingRule.applyTo(price);
    }

    isBuilt = true;

    return price;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  public PriceBuilder withPricingRule(PricingRule pricingRule) {
    pricingRules.add(pricingRule);
    return this;
  }

  /**
   * Add the Customer in case it affects the price (one permitted)
   *
   * @return The builder
   */
  public PriceBuilder withCustomer(Customer customer) {
    this.customer = Optional.fromNullable(customer);
    return this;
  }

  /**
   * Add the Supplier in case it affects the price (one permitted)
   *
   * @return The builder
   */
  public PriceBuilder withSupplier(Supplier supplier) {
    this.supplier = Optional.fromNullable(supplier);
    return this;
  }

  /**
   * @param startingPrice The starting price (default is GBP0.0)
   *
   * @return The builder
   */
  public PriceBuilder withStartingPrice(BigMoney startingPrice) {
    this.startingPrice = startingPrice;
    return this;
  }
}
