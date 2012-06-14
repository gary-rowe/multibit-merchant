package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.services.SecurityService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserResourceTest extends ResourceTest {

  private final SecurityService securityService =mock(SecurityService.class);
  private final User expectedUser = UserBuilder
    .getInstance()
    .build();


  @Override
  protected void setUpResources() {

    // Apply mocks
    when(securityService.getUserByUUID(anyString())).thenReturn(expectedUser);

    // Configure the test object
    UserResource testObject = new UserResource();
    testObject.setSecurityService(securityService);

    addResource(testObject);

  }

  @Test
  public void testGetByOpenId() throws Exception {

    User actualUser = client()
      .resource("/v1/user")
      .queryParam("openId", "abc123")
      .get(User.class);

    assertEquals("GET hello-world returns a default",expectedUser,actualUser);

  }

}
