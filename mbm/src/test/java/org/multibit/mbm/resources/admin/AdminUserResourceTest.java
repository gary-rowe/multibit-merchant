package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.user.AdminCreateUserRequest;
import org.multibit.mbm.api.request.user.AdminUpdateUserRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminUserResourceTest extends BaseJerseyResourceTest {

  private final UserDao userDao=mock(UserDao.class);

  private final AdminUserResource testObject=new AdminUserResource();

  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role adminRole = DatabaseLoader.buildAdminRole();
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the User for authenticated access
    User adminUser = setUpAuthenticator(Lists.newArrayList(adminRole));
    adminUser.setId(1L);

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
    // Create
    when(userDao.saveOrUpdate((User) isNotNull())).thenReturn(aliceUser);
    when(userDao.getByCredentials(anyString(), anyString())).thenReturn(Optional.<User>absent());
    // Retrieve
    when(userDao.getAllByPage(1,1)).thenReturn(usersPage1);
    when(userDao.getAllByPage(1,2)).thenReturn(usersPage2);
    // Update
    when(userDao.getById(1L)).thenReturn(Optional.of(aliceUser));

    testObject.setUserDao(userDao);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void adminCreateUserAsHalJson() throws Exception {

    AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest();
    createUserRequest.setUsername("charlie");
    createUserRequest.setPassword("charlie1");

    String actualResponse = client()
      .resource("/admin/user")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createUserRequest)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateUser by admin response render to HAL+JSON",actualResponse, "fixtures/hal/user/expected-admin-create-user.json");

  }

  @Test
  public void adminRetrieveUserAsHalJson() throws Exception {

    String actualResponse = client()
      .resource("/admin/user")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list 1 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/user/expected-admin-retrieve-users-page-1.json");

    actualResponse = client()
      .resource("/admin/user")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "2")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list 2 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/user/expected-admin-retrieve-users-page-2.json");

  }

  @Test
  public void adminUpdateUserAsHalJson() throws Exception {

    AdminUpdateUserRequest updateUserRequest = new AdminUpdateUserRequest();
    updateUserRequest.setUsername("charlie");
    updateUserRequest.setPassword("charlie1");

    String actualResponse = client()
      .resource("/admin/user/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateUserRequest)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateUser by admin response render to HAL+JSON",actualResponse, "fixtures/hal/user/expected-admin-update-user.json");

  }

  @Test
  public void adminDeleteUserAsHalJson() throws Exception {

    AdminDeleteEntityRequest deleteUserRequest = new AdminDeleteEntityRequest();
    deleteUserRequest.setReason("At user request");

    String actualResponse = client()
      .resource("/admin/user/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(deleteUserRequest)
      .delete(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("DeleteUser by admin response render to HAL+JSON",actualResponse, "fixtures/hal/user/expected-admin-delete-user.json");

  }

}
