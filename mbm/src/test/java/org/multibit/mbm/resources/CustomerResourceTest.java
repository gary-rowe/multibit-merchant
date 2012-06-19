package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerResourceTest extends BaseJerseyResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);
  private final Customer expectedCustomer = CustomerBuilder
    .newInstance()
    .build();
  private final User expectedUser = UserBuilder
    .newInstance()
    .withCustomer(expectedCustomer)
    .build();

  @Override
  protected void setUpResources() throws Exception {

    // Apply mocks
    when(customerService.findByOpenId(anyString())).thenReturn(expectedCustomer);

    // Configure the test object
    CustomerResource testObject = new CustomerResource();
    testObject.setCustomerService(customerService);

    addResource(testObject);

  }

  @Test
  public void testGetByOpenId() throws Exception {

    String actualResponse = client()
      .resource("/customer")
      .queryParam("openId", "abc123")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    String expectedResponse= FixtureHelpers.fixture("fixtures/hal/customer/expected-customer-simple-jersey.json");

    assertThat(actualResponse,is(equalTo(expectedResponse)));


  }

}
