package org.multibit.mbm.resources;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.RoleBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminUserResourceTest extends BaseJerseyResourceTest {

  private final UserDao userDao=mock(UserDao.class);

  private final AdminUserResource testObject=new AdminUserResource();

  @Override
  protected void setUpResources() {

    // Create the admin Role
    Role adminRole = RoleBuilder.newInstance()
      .withAdminAuthorities()
      .withName("Administrator")
      .withDescription("Administrator role")
      .build();

    Role customerRole = RoleBuilder.newInstance()
      .withCustomerAuthorities()
      .withName("Customer")
      .withDescription("Customer role")
      .build();

    // Create the User for authenticated access
    User adminUser = setUpAuthenticator(Lists.newArrayList(adminRole));
    adminUser.setId(1L);

    // Create pages
    List<User> usersPage1 = Lists.newArrayList();
    usersPage1.add(DatabaseLoader.buildCustomerAlice(customerRole));
    List<User> usersPage2 = Lists.newArrayList();
    usersPage1.add(DatabaseLoader.buildCustomerBob(customerRole));

    // Configure the mock DAO
    when(userDao.getAllByPage(1,1)).thenReturn(usersPage1);
    when(userDao.getAllByPage(1,2)).thenReturn(usersPage2);

    testObject.setUserDao(userDao);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void testGetAllByPage() throws Exception {

    String actualResponse = client()
      .resource("/admin/user")
      .queryParam("pageSize","1")
      .queryParam("pageNumber","1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list can be retrieved as JSON", actualResponse, "fixtures/hal/user/expected-users-by-admin-page-1.json");

    actualResponse = client()
      .resource("/admin/user")
      .queryParam("pageSize","1")
      .queryParam("pageNumber","2")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("User list can be retrieved as JSON", actualResponse, "fixtures/hal/user/expected-users-by-admin-page-2.json");

  }

}
