package org.multibit.mbm.security.dao;

import org.junit.Test;
import org.multibit.mbm.security.builder.RoleBuilder;
import org.multibit.mbm.security.dto.Authority;
import org.multibit.mbm.security.dto.Role;
import org.multibit.mbm.test.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateRoleDaoIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateRoleDao")
  RoleDao testObject;

  /**
   * Verifies Role creation and updates work against Authorities
   */
  @Test
  public void testPersistAndFind() {

    // Create a new Role
    Role expected = RoleBuilder.getInstance()
      .setName("ROLE_TEST")
      .setDescription("A test role")
      .addAuthority(Authority.CHANGE_OWN_PASSWORD)
      .build();

    // Persist with insert
    int originalRoleRows = countRowsInTable("roles");
    int originalAuthorityRows = countRowsInTable("authorities");
    testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect an insert in roles and authorities
    int updatedRoleRows = countRowsInTable("roles");
    int updatedAuthorityRows = countRowsInTable("authorities");
    assertThat("Expected session flush for first insert", updatedRoleRows, equalTo(originalRoleRows + 1));
    assertThat("Unexpected data in authorities", updatedAuthorityRows, equalTo(originalAuthorityRows + 1));

    // Perform an update to the Role that should not update authorities
    expected.setDescription("An updated test role");
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to roles, authorities
    updatedRoleRows = countRowsInTable("roles");
    updatedAuthorityRows = countRowsInTable("authorities");
    assertThat("Unexpected data in roles", updatedRoleRows, equalTo(originalRoleRows+1));
    assertThat("Unexpected data in authorities", updatedAuthorityRows, equalTo(originalAuthorityRows+1));

    // Query against the role name
    Role actual=testObject.getRoleByName("ROLE_TEST");

    // Session flush: Expect no change to roles, authorities
    updatedRoleRows = countRowsInTable("roles");
    updatedAuthorityRows = countRowsInTable("authorities");
    assertThat("Unexpected data in roles",updatedRoleRows, equalTo(originalRoleRows+1));
    assertThat("Unexpected data in authorities",updatedAuthorityRows, equalTo(originalAuthorityRows+1));

    assertThat(actual,equalTo(expected));

  }

  /**
   * Verifies that data loading occurred as expected
   */
  @Test
  public void testRolesAndAuthorities() {

    Role adminRole = testObject.getRoleByName("ROLE_ADMIN");
    
    assertNotNull("Expected pre-populated data",adminRole);
    assertThat("Unexpected number of Roles",adminRole.getAuthorities().size(), equalTo(Authority.values().length));

  }

}
