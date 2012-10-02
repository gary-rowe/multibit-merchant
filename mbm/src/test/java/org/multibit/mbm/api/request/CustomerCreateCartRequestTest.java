package org.multibit.mbm.api.request;

import com.yammer.dropwizard.testing.JsonHelpers;
import org.junit.Test;
import org.multibit.mbm.api.response.CustomerCartItem;
import org.multibit.mbm.test.FixtureAsserts;

public class CustomerCreateCartRequestTest {

  @Test
  public void testMarshal_Json() throws Exception {

    // Build the create cart request
    CustomerCreateCartRequest createCartRequest = new CustomerCreateCartRequest();
    createCartRequest.getCartItems().add(new CustomerCartItem(1L,1));
    createCartRequest.getCartItems().add(new CustomerCartItem(2L,2));

    String representation = JsonHelpers.asJson(createCartRequest);

    FixtureAsserts.assertStringMatchesJsonFixture("CustomerCreateCartRequest represented as JSON", representation, "fixtures/user/expected-customer-create-user-request.json");

  }

}
