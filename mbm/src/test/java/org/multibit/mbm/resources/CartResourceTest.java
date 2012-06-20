package org.multibit.mbm.resources;

import org.junit.Ignore;
import org.multibit.mbm.api.request.CreateCartRequest;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;

import javax.xml.ws.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class CartResourceTest extends BaseJerseyResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final Customer expectedCustomer = CustomerBuilder
    .newInstance()
    .build();

  private final CartResource testObject=new CartResource();


  @Override
  protected void setUpResources() {

    // Apply mocks
    when(customerService.findByOpenId(anyString())).thenReturn(expectedCustomer);

    // Configure the test object
    testObject.setCustomerService(customerService);

    setUpAuthenticator();

    // Configure resources
    addResource(testObject);

  }

  // TODO Implement this
  public void testCreateCart() throws Exception {

    CreateCartRequest request = new CreateCartRequest();

    Response actual = client()
      .resource("/v1/cart")
      .post(Response.class, request);

    //assertEquals("POST create cart",response, actual);



  }

}
