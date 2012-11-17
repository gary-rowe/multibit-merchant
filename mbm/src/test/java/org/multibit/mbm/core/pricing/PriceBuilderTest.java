package org.multibit.mbm.core.pricing;

import com.xeiam.xchange.utils.MoneyUtils;
import org.joda.money.BigMoney;
import org.junit.Test;
import org.multibit.mbm.core.model.*;
import org.multibit.mbm.db.DatabaseLoader;

import static junit.framework.Assert.assertEquals;

public class PriceBuilderTest {

  @Test
  public void testBuild() throws Exception {

    // Configure the Supplier
    Role supplierRole = DatabaseLoader.buildSupplierRole();
    User steveUser = DatabaseLoader.buildSteveSupplier(supplierRole);

    BigMoney unitPrice = MoneyUtils.parseFiat("GBP 1.23");

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
