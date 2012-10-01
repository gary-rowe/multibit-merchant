package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.CustomerCreateCartRequest;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import static org.mockito.Mockito.mock;

public class CartResourceTest extends BaseJerseyResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final CustomerCartResource testObject=new CustomerCartResource();


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
    addResource(testObject);

  }

  @Test
  public void testCreateCart() throws Exception {

    CustomerCreateCartRequest createCartRequest = new CustomerCreateCartRequest();

    String actualResponse = client()
      .resource("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createCartRequest)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateCart response render to JSON",actualResponse,"fixtures/hal/cart/expected-cart-new-jersey.json");

  }
}
