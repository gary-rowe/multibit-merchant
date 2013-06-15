package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.client.domain.repositories.RoleReadService;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;
import org.multibit.mbm.client.domain.model.model.Role;
import org.multibit.mbm.client.domain.model.model.RoleBuilder;
import org.multibit.mbm.testing.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateRoleReadServiceIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateRoleDao")
  RoleReadService testObject;

  /**
   * Verifies Role creation and updates work against Authorities
   */
  @Test
  public void testPersistAndFind() {

    // Create a new Role
    Role expected = RoleBuilder.newInstance()
      .withName("ROLE_TEST")
      .withDescription("A test role")
      .withAuthority(Authority.CHANGE_OWN_PASSWORD)
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
    Optional<Role> actual=testObject.getByName("ROLE_TEST");

    // Session flush: Expect no change to roles, authorities
    updatedRoleRows = countRowsInTable("roles");
    updatedAuthorityRows = countRowsInTable("authorities");
    assertThat("Unexpected data in roles",updatedRoleRows, equalTo(originalRoleRows+1));
    assertThat("Unexpected data in authorities",updatedAuthorityRows, equalTo(originalAuthorityRows+1));

    assertThat(actual.get(),equalTo(expected));

  }

  /**
   * Verifies that data loading occurred as expected
   */
  @Test
  public void testRolesAndAuthorities() {

    Optional<Role> adminRole = testObject.getByName("ROLE_ADMIN");
    
    assertTrue("Expected pre-populated data", adminRole.isPresent());
    assertThat("Unexpected number of Roles",adminRole.get().getAuthorities().size(), equalTo(Authority.values().length));

  }

}
