package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.multibit.mbm.customer.builder.CustomerBuilder;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.services.CustomerService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerResourceTest extends ResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);
  private final Customer expectedCustomer = CustomerBuilder
    .getInstance()
    .build();


  @Override
  protected void setUpResources() {

    // Apply mocks
    when(customerService.findByOpenId(anyString())).thenReturn(expectedCustomer);

    // Configure the test object
    CustomerResource testObject = new CustomerResource();
    testObject.setCustomerService(customerService);

    addResource(testObject);

  }

  @Test
  public void testGetByOpenId() throws Exception {

    Customer actualCustomer = client()
      .resource("/v1/customer")
      .queryParam("openId", "abc123")
      .get(Customer.class);

    assertEquals("GET hello-world returns a default",expectedCustomer,actualCustomer);

  }

}
