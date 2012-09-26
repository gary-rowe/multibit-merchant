package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.CreateCartRequest;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartResourceTest extends BaseJerseyResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final CartResource testObject=new CartResource();


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
  public void testCreateCart() throws Exception {

    CreateCartRequest createCartRequest = new CreateCartRequest();
    createCartRequest.setSessionId("1234");

    String actualResponse = client()
      .resource("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createCartRequest)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateCart response render to JSON",actualResponse,"fixtures/hal/cart/expected-cart-new-jersey.json");

  }
}
