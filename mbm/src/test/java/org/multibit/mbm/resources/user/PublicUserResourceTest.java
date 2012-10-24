package org.multibit.mbm.resources.user;

import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

/**
 * Verifies the user resource can be accessed by the anonymous public
 */
public class PublicUserResourceTest extends BaseJerseyHmacResourceTest {

  private final PublicUserResource testObject=new PublicUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User publicUser = setUpPublicHmacAuthenticator();
    publicUser.setId(1L);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void publicRetrieveUserAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/user")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Public retrieve their User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-public-retrieve-user.json");

  }

}
