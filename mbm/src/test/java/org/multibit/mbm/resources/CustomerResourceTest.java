package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import static org.mockito.Mockito.mock;

public class CustomerResourceTest extends BaseJerseyResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final CustomerResource testObject=new CustomerResource();


  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User user = setUpAuthenticator();
    user.setId(1L);
    user.getCustomer().setId(1L);
    user.getCustomer().getCart().setId(1L);

    // Configure the test object
    testObject.setCustomerService(customerService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void retrieveCustomerUserAsJson() throws Exception {

    String actualResponse = client()
      .resource("/customer")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Customer can be retrieved as JSON",actualResponse, "fixtures/hal/customer/expected-customer-retrieve-customer.json");

  }

}
