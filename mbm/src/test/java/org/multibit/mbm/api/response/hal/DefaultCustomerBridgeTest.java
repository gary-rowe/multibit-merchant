package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.junit.Before;
import org.junit.Test;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseResourceTest;
import org.multibit.mbm.test.TestUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class DefaultCustomerBridgeTest extends BaseResourceTest {

  @Before
  public void setUpDefaultRequestInfo() throws Exception {
    setUpUriInfo(Optional.<String>absent());
    setUpHttpHeaders(Optional.<List<MediaType>>absent());
  }

  @Test
  public void representCustomerAsJson() throws Exception {


    Customer customer = CustomerBuilder
      .getInstance()
      .build();

    Optional<User> user = Optional.absent();

    DefaultCustomerBridge testObject = new DefaultCustomerBridge(uriInfo,user);

    Resource resource = testObject.toResource(customer);

    TestUtils.assertResourceMatchesJsonFixture("a Customer can be marshalled to JSON", resource, "fixtures/hal/customer/expected-customer-simple.json");

  }

  @Test
  public void representCustomerAsXml() throws IOException {

    Customer customer = CustomerBuilder
      .getInstance()
      .build();

    Optional<User> user = Optional.absent();

    DefaultCustomerBridge testObject = new DefaultCustomerBridge(uriInfo,user);

    Resource resource = testObject.toResource(customer);

    TestUtils.assertResourceMatchesXmlFixture("a Customer can be marshalled to JSON", resource, "fixtures/hal/customer/expected-customer-simple.xml");

  }

}
