package org.multibit.mbm.interfaces.rest.resources.user;

import org.junit.Test;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

/**
 * Verifies the user resource can be accessed by an authenticated Supplier
 */
public class SupplierUserResourceTest extends BaseJerseyHmacResourceTest {

  private final SupplierUserResource testObject=new SupplierUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User clientUser = setUpSteveHmacAuthenticator();
    clientUser.setId(1L);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void supplierRetrieveUserAsHalJson() throws Exception {

    String actualResponse = configureAsClient(SupplierUserResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Supplier retrieve their User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-supplier-retrieve-user.json");

  }

}
