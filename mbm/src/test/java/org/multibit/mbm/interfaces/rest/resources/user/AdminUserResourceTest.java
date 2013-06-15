package org.multibit.mbm.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.common.pagination.PaginatedLists;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.UserReadService;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.interfaces.rest.api.AdminDeleteEntityDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.user.AdminCreateUserDto;
import org.multibit.mbm.interfaces.rest.api.user.AdminUpdateUserDto;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminUserResourceTest extends BaseJerseyHmacResourceTest {

  private final UserReadService userReadService = mock(UserReadService.class);

  private final AdminUserResource testObject = new AdminUserResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User adminUser = setUpTrentHmacAuthenticator();
    adminUser.setId(1L);

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the customer Users
    User aliceUser = DatabaseLoader.buildAliceCustomer(customerRole);
    aliceUser.setId(1L);
    User bobUser = DatabaseLoader.buildBobCustomer(customerRole);
    bobUser.setId(2L);

    // Create pages
    List<User> usersPage1 = Lists.newArrayList();
    usersPage1.add(aliceUser);
    List<User> usersPage2 = Lists.newArrayList();
    usersPage2.add(bobUser);

    // Configure the mock DAO
    PaginatedList<User> page1 = PaginatedLists.newPaginatedArrayList(1, 2, 1, usersPage1);
    PaginatedList<User> page2 = PaginatedLists.newPaginatedArrayList(2, 2, 1, usersPage2);
    // Create
    when(userReadService.saveOrUpdate((User) isNotNull())).thenReturn(aliceUser);
    when(userReadService.getByCredentials(anyString(), anyString())).thenReturn(Optional.<User>absent());
    // Retrieve
    when(userReadService.getPaginatedList(1, 0)).thenReturn(page1);
    when(userReadService.getPaginatedList(1, 1)).thenReturn(page2);
    // Update
    when(userReadService.getById(1L)).thenReturn(Optional.of(aliceUser));

    testObject.setUserReadService(userReadService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void adminCreateUserAsHalJson() throws Exception {

    AdminCreateUserDto createUserRequest = new AdminCreateUserDto();
    createUserRequest.setUsername("charlie");
    createUserRequest.setPasswordDigest("charlie1");

    String actualResponse = configureAsClient("/admin/user")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createUserRequest, MediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateUser by admin response render to HAL+JSON", actualResponse, "/fixtures/hal/user/expected-admin-create-user.json");

  }

  @Test
  public void adminRetrieveUserAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/admin/user")
      .queryParam("ps", "1")
      .queryParam("pn", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-admin-retrieve-users-page-1.json");

    actualResponse = configureAsClient("/admin/user")
      .queryParam("ps", "1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/user/expected-admin-retrieve-users-page-2.json");

  }

  @Test
  public void adminUpdateUserAsHalJson() throws Exception {

    AdminUpdateUserDto updateUserRequest = new AdminUpdateUserDto();
    updateUserRequest.setUsername("charlie");
    updateUserRequest.setPasswordDigest("charlie1");

    String actualResponse = configureAsClient("/admin/user/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateUserRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateUser by admin response render to HAL+JSON", actualResponse, "/fixtures/hal/user/expected-admin-update-user.json");

  }

  @Test
  public void adminDeleteUserAsHalJson() throws Exception {

    AdminDeleteEntityDto deleteUserRequest = new AdminDeleteEntityDto();
    deleteUserRequest.setReason("At user request");

    String actualResponse = configureAsClient("/admin/user/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(deleteUserRequest, MediaType.APPLICATION_JSON_TYPE)
      .delete(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("DeleteUser by admin response render to HAL+JSON", actualResponse, "/fixtures/hal/user/expected-admin-delete-user.json");

  }

}
