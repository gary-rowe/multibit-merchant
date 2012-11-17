package org.multibit.mbm.resources.user;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

/**
 * Verifies the user resource can be accessed by an authenticated Customer
 */
public class CustomerUserResourceTest extends BaseJerseyHmacResourceTest {

  private final CustomerUserResource testObject=new CustomerUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User clientUser = setUpAliceHmacAuthenticator();
    clientUser.setId(1L);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void customerRetrieveUserAsHalJson() throws Exception {

    String actualResponse = configureAsClient(CustomerUserResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Customer retrieve their User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-customer-retrieve-user.json");

  }

}
