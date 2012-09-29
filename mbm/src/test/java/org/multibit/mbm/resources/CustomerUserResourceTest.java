package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

public class CustomerUserResourceTest extends BaseJerseyResourceTest {

  private final CustomerUserResource testObject=new CustomerUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User user = setUpAuthenticator();
    user.setId(1L);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void testRetrieveUser() throws Exception {

    String actualResponse = client()
      .resource("/user")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User can be retrieved as JSON", actualResponse, "fixtures/hal/user/expected-user-simple-jersey.json");

  }

}
