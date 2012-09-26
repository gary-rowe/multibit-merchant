package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.services.SecurityService;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserResourceTest extends BaseJerseyResourceTest {

  private final UserResource testObject=new UserResource();

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
