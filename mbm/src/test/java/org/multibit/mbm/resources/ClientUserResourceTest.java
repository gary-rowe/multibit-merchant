package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.user.WebFormAuthenticationRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientUserResourceTest extends BaseJerseyHmacResourceTest {

  private final UserDao userDao=mock(UserDao.class);

  private final ClientUserResource testObject=new ClientUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User clientUser = setUpClientHmacAuthenticator();
    clientUser.setId(1L);

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();
    User aliceUser = DatabaseLoader.buildAliceCustomer(customerRole);

    // Configure mocks
    when(userDao.getByCredentials(anyString(), anyString())).thenReturn(Optional.of(aliceUser));

    // Bind mocks
    testObject.setUserDao(userDao);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void clientAuthenticateUserAsHalJson() throws Exception {

    // Arrange
    WebFormAuthenticationRequest authenticateUserRequest = new WebFormAuthenticationRequest();
    authenticateUserRequest.setUsername("alice");
    authenticateUserRequest.setPasswordDigest("alice1");

    // Act
    String actualResponse = configureAsClient("/client/user/authenticate")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(authenticateUserRequest, MediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    // Assert
    FixtureAsserts.assertStringMatchesJsonFixture("Client authenticate their User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-client-authenticate-user.json");

  }

}
