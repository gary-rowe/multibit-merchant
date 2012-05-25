package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.ResourceTest;
import org.multibit.mbm.persistence.dto.CustomerBuilder;
import org.multibit.mbm.persistence.dto.Customer;
import org.multibit.mbm.services.CustomerService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartResourceTest extends ResourceTest {

  private final CustomerService customerService=mock(CustomerService.class);

  private final Customer expectedCustomer = CustomerBuilder
    .getInstance()
    .build();



  @Override
  protected void setUpResources() {

    // Apply mocks
    when(customerService.findByOpenId(anyString())).thenReturn(expectedCustomer);

    // Configure the test object
    CartResource testObject = new CartResource();
    testObject.setCustomerService(customerService);

    addResource(testObject);

  }

}
