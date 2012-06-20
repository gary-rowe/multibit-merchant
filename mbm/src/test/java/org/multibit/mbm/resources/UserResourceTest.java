package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.services.SecurityService;
import org.multibit.mbm.test.BaseJerseyResourceTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserResourceTest extends BaseJerseyResourceTest {

  private final SecurityService securityService =mock(SecurityService.class);
  private final User expectedUser = UserBuilder
    .newInstance()
    .build();


  @Override
  protected void setUpResources() {

    // Apply mocks
    when(securityService.getUserByUUID(anyString())).thenReturn(expectedUser);

    // Configure the test object
    UserResource testObject = new UserResource();
    testObject.setSecurityService(securityService);

    addResource(testObject);

    setUpAuthenticator();

  }

  @Test
  public void testGetByUuid() throws Exception {

    String actualResponse = client()
      .resource("/user")
      .queryParam("uuid", "abcd-1234")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    String expectedResponse= FixtureHelpers.fixture("fixtures/hal/user/expected-user-simple-jersey.json");

    assertThat(actualResponse,is(equalTo(expectedResponse)));
  }

}
