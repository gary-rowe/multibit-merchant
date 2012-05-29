package org.multibit.mbm.resources;

import org.junit.Ignore;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.persistence.dto.CustomerBuilder;
import org.multibit.mbm.rest.v1.client.cart.CreateCartRequest;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseResourceTest;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartResourceTest extends BaseResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final Customer expectedCustomer = CustomerBuilder
    .getInstance()
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

  @Ignore
  public void testCreateCart() throws Exception {

    String contents = "";
    String authorization = buildHmacAuthorization(contents, "abc123", "def456");

    CreateCartRequest request = new CreateCartRequest();

    Response actual = client()
      .resource("/v1/cart")
      .header(HttpHeaders.AUTHORIZATION, authorization)
      .post(Response.class, request);

    //assertEquals("POST create cart",response, actual);



  }

}
