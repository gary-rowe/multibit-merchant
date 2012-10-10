package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.role.AdminCreateRoleRequest;
import org.multibit.mbm.api.request.role.AdminUpdateRoleRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.RoleBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminRoleResourceTest extends BaseJerseyResourceTest {

  private final RoleDao roleDao=mock(RoleDao.class);

  private final AdminRoleResource testObject=new AdminRoleResource();

  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role adminRole = DatabaseLoader.buildAdminRole();
    adminRole.setId(1L);
    Role customerRole = DatabaseLoader.buildCustomerRole();
    customerRole.setId(2L);

    // Mimics the behaviour of the created Role
    Role newRole = RoleBuilder
      .newInstance()
      .withName("Stock Manager")
      .withDescription("Stock Manager roles")
      .build();
    newRole.setId(3L);

    // Create the User for authenticated access
    User adminUser = setUpAuthenticator(Lists.newArrayList(adminRole));
    adminUser.setId(1L);

    // Create pages
    List<Role> rolesPage1 = Lists.newArrayList();
    rolesPage1.add(adminRole);
    List<Role> rolesPage2 = Lists.newArrayList();
    rolesPage2.add(customerRole);

    // Configure the mock DAO
    // Create
    when(roleDao.saveOrUpdate((Role) isNotNull())).thenReturn(newRole);
    when(roleDao.getByName("Stock Manager")).thenReturn(Optional.<Role>absent());
    // Retrieve
    when(roleDao.getAllByPage(1, 1)).thenReturn(rolesPage1);
    when(roleDao.getAllByPage(1, 2)).thenReturn(rolesPage2);
    // Update
    when(roleDao.getById(1L)).thenReturn(Optional.of(adminRole));

    testObject.setRoleDao(roleDao);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void adminCreateRoleAsHalJson() throws Exception {

    AdminCreateRoleRequest createRoleRequest = new AdminCreateRoleRequest();
    createRoleRequest.setName("Stock Manager");
    createRoleRequest.setDescription("Stock Manager roles");

    String actualResponse = client()
      .resource("/admin/role")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createRoleRequest, MediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateRole by admin response render to HAL+JSON",actualResponse, "fixtures/hal/role/expected-admin-create-role.json");

  }

  @Test
  public void adminRetrieveRoleAsHalJson() throws Exception {

    String actualResponse = client()
      .resource("/admin/role")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Role list 1 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/role/expected-admin-retrieve-roles-page-1.json");

    actualResponse = client()
      .resource("/admin/role")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "2")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Role list 2 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/role/expected-admin-retrieve-roles-page-2.json");

  }

  @Test
  public void adminUpdateRoleAsHalJson() throws Exception {

    AdminUpdateRoleRequest updateRoleRequest = new AdminUpdateRoleRequest();
    updateRoleRequest.setName("charlie");
    updateRoleRequest.setDescription("charlie1");
    // TODO Re-instate this
    // updateRoleRequest.getAuthorities().add(Authority.ROLE_STORES_MANAGER.name());

    String actualResponse = client()
      .resource("/admin/role/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateRoleRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateRole by admin response render to HAL+JSON",actualResponse, "fixtures/hal/role/expected-admin-update-role.json");

  }

  @Test
  public void adminDeleteRoleAsHalJson() throws Exception {

    AdminDeleteEntityRequest deleteRoleRequest = new AdminDeleteEntityRequest();
    deleteRoleRequest.setReason("At role request");

    String actualResponse = client()
      .resource("/admin/role/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(deleteRoleRequest, MediaType.APPLICATION_JSON_TYPE)
      .delete(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("DeleteRole by admin response render to HAL+JSON",actualResponse, "fixtures/hal/role/expected-admin-delete-role.json");

  }

}
