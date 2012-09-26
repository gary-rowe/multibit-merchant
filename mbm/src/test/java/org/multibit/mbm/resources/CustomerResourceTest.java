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

  private final CustomerResource testObject=new CustomerResource();


  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User user = setUpAuthenticator();

    // Configure the customer in more detail
    Customer customer = user.getCustomer();
    customer.setId(1L);
    customer.getCart().setId(1L);
    when(customerService.findByOpenId(anyString())).thenReturn(customer);

    // Configure the test object
    testObject.setCustomerService(customerService);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void testGetCustomer() throws Exception {

    String actualResponse = client()
      .resource("/customer")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    String expectedResponse= FixtureHelpers.fixture("fixtures/hal/customer/expected-customer-simple-jersey.json");

    assertThat(actualResponse,is(equalTo(expectedResponse)));


  }

}
