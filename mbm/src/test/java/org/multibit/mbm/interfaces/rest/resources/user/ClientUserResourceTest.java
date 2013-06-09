package org.multibit.mbm.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.interfaces.rest.api.user.WebFormAuthenticationDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.repositories.RoleReadService;
import org.multibit.mbm.domain.repositories.UserReadService;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies the user resource can be accessed by an authenticated Customer
 */
public class ClientUserResourceTest extends BaseJerseyHmacResourceTest {

  private final UserReadService userReadService =mock(UserReadService.class);
  private final RoleReadService roleReadService =mock(RoleReadService.class);

  private final ClientUserResource testObject=new ClientUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User clientUser = setUpClientHmacAuthenticator();
    clientUser.setId(1L);

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();
    Role publicRole = DatabaseLoader.buildPublicRole();
    User aliceUser = DatabaseLoader.buildAliceCustomer(customerRole);
    User bobUser = DatabaseLoader.buildBobCustomer(customerRole);

    // Configure mocks
    when(userReadService.getByCredentials(anyString(), anyString())).thenReturn(Optional.of(aliceUser));
    when(userReadService.saveOrUpdate(any(User.class))).thenReturn(bobUser);
    when(roleReadService.getByName(Authority.ROLE_PUBLIC.name())).thenReturn(Optional.of(publicRole));

    // Bind mocks
    testObject.setUserReadService(userReadService);
    testObject.setRoleReadService(roleReadService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void clientRegisterAnonymousUserAsHalJson() throws Exception {

    // Arrange

    // Act
    String actualResponse = configureAsClient("/client/user/anonymous")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .post(String.class);

    // Assert
    FixtureAsserts.assertStringMatchesJsonFixture("Client register their anonymous User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-client-register-anonymous-user.json");

  }

  @Test
  public void clientAuthenticateUserAsHalJson() throws Exception {

    // Arrange
    WebFormAuthenticationDto authenticateUserRequest = new WebFormAuthenticationDto(
      "alice",
      "alice1"
    );

    // Act
    String actualResponse = configureAsClient("/client/user/authenticate")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(authenticateUserRequest, MediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    // Assert
    FixtureAsserts.assertStringMatchesJsonFixture("Client authenticate their User as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-client-authenticate-user.json");

  }

}

