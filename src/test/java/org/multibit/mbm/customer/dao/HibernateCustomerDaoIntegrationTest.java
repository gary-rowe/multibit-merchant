package org.multibit.mbm.customer.dao;

import org.junit.Test;
import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 * TODO Add in the CustomerBuilder
 */
@ContextConfiguration(locations = {"/spring/test-mbm-hibernate-dao.xml"})
public class HibernateCustomerDaoIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Resource(name= "hibernateCustomerDao")
  CustomerDao testObject;

  @Test
  public void testPersistAndFind() {

    String openId="abc123";
    Customer expected = new Customer();
    expected.setOpenId(openId);

    // Persist with insert
    int originalCustomerRows = countRowsInTable("customers");
    int originalContactMethodDetailRows = countRowsInTable("contact_method_details");
    int originalContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    testObject.persist(expected);

    // Session flush: Expect an insert in customers only
    int updatedCustomerRows = countRowsInTable("customers");
    int updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    int updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Expected session flush for first insert", updatedCustomerRows, equalTo(originalCustomerRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the Customer that cascades to an insert in ContactMethod (but not secondary)
    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail("test@example.org");
    expected.setContactMethodDetail(ContactMethod.EMAIL, contactMethodDetail);
    expected=testObject.persist(expected);

    // Session flush: Expect no change to customers, insert into contact_method_details
    // Note that contactMethodDetail is now a different instance from the persistent one
    updatedCustomerRows = countRowsInTable("customers");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows+1));
    assertThat("Expected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the Customer that cascades to an insert in secondary ContactMethod
    // due to an addition to the linked reference
    contactMethodDetail = expected.getContactMethodDetail(ContactMethod.EMAIL);
    contactMethodDetail.getSecondaryDetails().add("test2@example.org");
    expected=testObject.persist(expected);

    // No session flush: Expect no change to customers, contact_method_details, contact_method_secondary_details
    updatedCustomerRows = countRowsInTable("customers");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected session flush in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Force a flush
    testObject.flush();

    // Query against the "openId"
    Customer actual=testObject.getCustomerByOpenId("abc123");

    // Session flush: Expect no change to customers, contact_method_details, insert into contact_method_secondary_details
    updatedCustomerRows = countRowsInTable("customers");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in customers",updatedCustomerRows, equalTo(originalCustomerRows+1));
    assertThat("Unexpected data in contact_method_details",updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Expected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows+1));

    assertThat(actual,equalTo(expected));


  }

}
