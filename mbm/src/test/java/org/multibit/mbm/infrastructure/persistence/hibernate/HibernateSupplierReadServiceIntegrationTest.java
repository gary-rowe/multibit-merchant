package org.multibit.mbm.infrastructure.persistence.hibernate;

import org.junit.Test;
import org.multibit.mbm.domain.repositories.SupplierReadService;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.model.model.Supplier;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.testing.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateSupplierReadServiceIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateSupplierDao")
  SupplierReadService testObject;

  @Test
  public void testPersistAndFind() {

    Role supplierRole = DatabaseLoader.buildSupplierRole();
    User steveUser = DatabaseLoader.buildSteveSupplier(supplierRole);

    Supplier expected = steveUser.getSupplier();

    // Persist with insert
    int originalSupplierRows = countRowsInTable("suppliers");
    testObject.saveOrUpdate(expected);

    // Session flush: Expect an insert in suppliers only
    int updatedSupplierRows = countRowsInTable("suppliers");
    assertThat("Expected session flush for first insert", updatedSupplierRows, equalTo(originalSupplierRows+1));

    // Perform an update to the Supplier that cascades to an insert in ContactMethod (but not secondary)
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to suppliers, insert into contact_method_details
    // Note that contactMethodDetail is now a different instance from the persistent one
    updatedSupplierRows = countRowsInTable("suppliers");
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows+1));

    // Perform an update to the Supplier that cascades to an insert in secondary ContactMethod
    // due to an addition to the linked reference
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to suppliers, contact_method_details, insert into contact_method_secondary_details
    updatedSupplierRows = countRowsInTable("suppliers");
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows+1));

  }

}
