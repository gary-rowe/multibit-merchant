package org.multibit.mbm.api.request.admin;

import com.yammer.dropwizard.testing.JsonHelpers;
import org.junit.Test;
import org.multibit.mbm.test.FixtureAsserts;

public class AdminCreateUserRequestTest {

  @Test
  public void testMarshal_Json() throws Exception {

    AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest();
    createUserRequest.setUsername("charlie");
    createUserRequest.setPassword("charlie1");
    createUserRequest.setOpenId("abc123");
    createUserRequest.setOneTimeUse(true);

    String representation = JsonHelpers.asJson(createUserRequest);

    FixtureAsserts.assertStringMatchesJsonFixture("AdminCreateUserRequest represented as JSON", representation, "fixtures/user/expected-admin-create-user-request.json");
  }

}
