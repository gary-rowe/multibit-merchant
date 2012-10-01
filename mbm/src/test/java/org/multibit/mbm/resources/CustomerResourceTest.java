package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dto.Role;
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

    // Configure the customer in more detail
    Role customerRole = DatabaseLoader.buildCustomerRole();
    User aliceUser = DatabaseLoader.buildAliceCustomer(customerRole);
    aliceUser.setId(1L);
    aliceUser.getCustomer().setId(1L);
    aliceUser.getCustomer().getCart().setId(1L);

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

    FixtureAsserts.assertStringMatchesJsonFixture("Customer can be retrieved as JSON",actualResponse, "fixtures/hal/customer/expected-customer-simple-jersey.json");

  }

}
