package org.multibit.mbm.api.response.hal;

import com.theoryinpractise.halbuilder.spi.Resource;
import org.junit.Test;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.test.TestUtils;

import java.io.IOException;

public class DefaultCustomerBridgeTest {

  @Test
  public void representCustomer() throws IOException {

    Customer customer = CustomerBuilder
      .getInstance()
      .build();

    DefaultCustomerBridge testObject = new DefaultCustomerBridge("http://example.org");

    Resource resource = testObject.toResource(customer);

    TestUtils.assertResourceMatchesJsonFixture("a Customer can be marshalled to JSON", resource);

  }

}
