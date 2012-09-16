package org.multibit.mbm.api.request;

import com.yammer.dropwizard.testing.FixtureHelpers;
import com.yammer.dropwizard.testing.JsonHelpers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateCartRequestTest {

  @Test
  public void testMarshal_Json() throws Exception {

    CreateCartRequest createCartRequest = new CreateCartRequest();
    createCartRequest.setSessionId("1234");

    String actualJson = JsonHelpers.asJson(createCartRequest);

    String expectedJson= FixtureHelpers.fixture("fixtures/hal/cart/expected-create-cart-request.json");

    MatcherAssert.assertThat(actualJson, is(equalTo(expectedJson)));
  }

}
