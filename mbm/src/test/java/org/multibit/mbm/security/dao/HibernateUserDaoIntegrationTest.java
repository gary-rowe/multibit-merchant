package org.multibit.mbm.security.dao;

import org.junit.Test;
import org.multibit.mbm.security.builder.UserBuilder;
import org.multibit.mbm.security.dto.ContactMethod;
import org.multibit.mbm.security.dto.ContactMethodDetail;
import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.test.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateUserDaoIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateUserDao")
  UserDao testObject;

  @Test
  public void testPersistAndFind() {

    String openId="abc123";
    String uuid="1234-5678";

    User expected = UserBuilder.getInstance()
      .setOpenId(openId)
      .setUUID(uuid)
      .build();
    expected.setOpenId(openId);

    // Persist with insert
    int originalUserRows = countRowsInTable("users");
    int originalContactMethodDetailRows = countRowsInTable("contact_method_details");
    int originalContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    testObject.saveOrUpdate(expected);

    // Session flush: Expect an insert in users only
    int updatedUserRows = countRowsInTable("users");
    int updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    int updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Expected session flush for first insert", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the User that cascades to an insert in ContactMethod (but not secondary)
    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail("test@example.org");
    expected.setContactMethodDetail(ContactMethod.EMAIL, contactMethodDetail);
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to users, insert into contact_method_details
    // Note that contactMethodDetail is now a different instance from the persistent one
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Expected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the User that cascades to an insert in secondary ContactMethod
    // due to an addition to the linked reference
    contactMethodDetail = expected.getContactMethodDetail(ContactMethod.EMAIL);
    contactMethodDetail.getSecondaryDetails().add("test2@example.org");
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to users, contact_method_details, insert into contact_method_secondary_details
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows+1));

    // Query against the "openId"
    User actual=testObject.getUserByOpenId("abc123");

    // Session flush: Expect no change to users, contact_method_details, contact_method_secondary_details
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users",updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details",updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows+1));

    assertThat(actual,equalTo(expected));

  }

  @Test
  public void testRolesAndAuthorities() {

    String openId="abc123";
    String uuid="1234-5678";

    User expected = UserBuilder.getInstance()
      .setOpenId(openId)
      .setUUID(uuid)
      .build();
    expected.setOpenId(openId);

    // Persist with insert
    int originalUserRows = countRowsInTable("users");
    int originalContactMethodDetailRows = countRowsInTable("contact_method_details");
    int originalContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    testObject.saveOrUpdate(expected);

    // Session flush: Expect an insert in users only
    int updatedUserRows = countRowsInTable("users");
    int updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    int updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Expected session flush for first insert", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the User that cascades to an insert in ContactMethod (but not secondary)
    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail("test@example.org");
    expected.setContactMethodDetail(ContactMethod.EMAIL, contactMethodDetail);
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to users, insert into contact_method_details
    // Note that contactMethodDetail is now a different instance from the persistent one
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Expected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows));

    // Perform an update to the User that cascades to an insert in secondary ContactMethod
    // due to an addition to the linked reference
    contactMethodDetail = expected.getContactMethodDetail(ContactMethod.EMAIL);
    contactMethodDetail.getSecondaryDetails().add("test2@example.org");
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to users, contact_method_details, insert into contact_method_secondary_details
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users", updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details", updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows+1));

    // Query against the "openId"
    User actual=testObject.getUserByOpenId("abc123");

    // Session flush: Expect no change to users, contact_method_details, contact_method_secondary_details
    updatedUserRows = countRowsInTable("users");
    updatedContactMethodDetailRows = countRowsInTable("contact_method_details");
    updatedContactMethodDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Unexpected data in users",updatedUserRows, equalTo(originalUserRows+1));
    assertThat("Unexpected data in contact_method_details",updatedContactMethodDetailRows, equalTo(originalContactMethodDetailRows+1));
    assertThat("Unexpected data in contact_method_secondary_details", updatedContactMethodDetailSecondaryRows, equalTo(originalContactMethodDetailSecondaryRows+1));

    assertThat(actual,equalTo(expected));

  }

}
