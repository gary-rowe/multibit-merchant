package org.multibit.mbm.domain.model.pricing;

import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;
import org.junit.Test;
import org.multibit.mbm.domain.model.model.PricingRule;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;

import static junit.framework.Assert.assertEquals;

public class PriceBuilderTest {

  @Test
  public void testBuild() throws Exception {

    // Configure the Supplier
    Role supplierRole = DatabaseLoader.buildSupplierRole();
    User steveUser = DatabaseLoader.buildSteveSupplier(supplierRole);

    BigMoney unitPrice = MoneyUtils.parse("GBP 1.23");

    // Configure some PricingRules
    PricingRule pricingRule = DatabaseLoader.buildPresetMarginPricingRule();

    BigMoney price = PriceBuilder
      .newInstance()
      .withSupplier(steveUser.getSupplier())
      .withPricingRule(pricingRule)
      .withStartingPrice(unitPrice)
      .build();

    assertEquals("GBP 1.476",price.toString());

  }
}
