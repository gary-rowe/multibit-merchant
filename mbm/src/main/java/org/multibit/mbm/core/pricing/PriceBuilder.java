package org.multibit.mbm.core.pricing;

import com.google.common.collect.Lists;
import org.joda.money.BigMoney;
import org.joda.time.DateTime;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.core.model.Customer;
import org.multibit.mbm.core.model.Supplier;
import org.multibit.mbm.utils.DateUtils;

import javax.annotation.Resource;
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

  @Resource(name="hibernateItemDao")
  private ItemDao itemDao;

  private int quantity = 0;
  private DateTime start = DateUtils.nowUtc();
  private DateTime end = DateUtils.nowUtc();
  private String currencyCode = "GBP";
  private Customer customer;
  private Supplier supplier;

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
    BigMoney price=null;

    for (PricingRule pricingRule : pricingRules) {
      if (pricingRule.canProcess(customer)) {
        price = pricingRule.applyTo(price, customer);
      }
      if (pricingRule.canProcess(supplier)) {
        price = pricingRule.applyTo(price, customer);
      }
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
   * Add the Customer to the Price (one permitted)
   *
   * @return The builder
   */
  public PriceBuilder withCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }

  /**
   * Add the Supplied to the Price (one permitted)
   *
   * @return The builder
   */
  public PriceBuilder withSupplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
