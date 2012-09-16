package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.FixtureHelpers;
import com.yammer.dropwizard.testing.JsonHelpers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.CreateCartRequest;
import org.multibit.mbm.core.Saying;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.services.CustomerService;
import org.multibit.mbm.test.BaseJerseyResourceTest;

import javax.xml.ws.Response;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

  @Test
  public void testCreateCart() throws Exception {

    CreateCartRequest createCartRequest = new CreateCartRequest();

    // TODO Fix the canonical representation problem for POST

    CreateCartRequest actualResponse = client()
      .resource("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createCartRequest)
      .post(CreateCartRequest.class);

    CreateCartRequest expectedResponse= JsonHelpers.fromJson(FixtureHelpers.fixture("fixtures/hal/cart/expected-cart-new-jersey.json"),CreateCartRequest.class);

    assertThat(actualResponse,is(equalTo(expectedResponse)));

  }
}
