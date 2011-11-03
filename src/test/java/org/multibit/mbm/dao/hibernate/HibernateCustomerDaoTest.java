package org.multibit.mbm.dao.hibernate;

import org.junit.Test;
import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.domain.Customer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = {"/spring/test-mbm-hibernate-dao.xml"})
public class HibernateCustomerDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Resource(name= "hibernateCustomerDao")
  CustomerDao testObject;

  @Test
  public void testPersistAndFind() {

    String openId="abc123";
    Customer expected = new Customer();
    expected.setOpenId(openId);

    // Persist with insert
    int originalRows = countRowsInTable("customers");
    testObject.persist(expected);

    int updatedRows = countRowsInTable("customers");
    assertThat("Expected session flush for first insert",updatedRows, equalTo(originalRows+1));

    // Perform an update
    expected.setEmailAddress("test@example.org");
    testObject.persist(expected);

    updatedRows = countRowsInTable("customers");
    assertThat("Unexpected session flush for update",updatedRows, equalTo(originalRows+1));

    // Query against the "openId"
    Customer actual=testObject.getCustomerByOpenId("abc123");

    // The above should cause a session flush to enable consistent state
    updatedRows = countRowsInTable("customers");
    assertThat("Unexpected insert for update operation",updatedRows, equalTo(originalRows+1));

    assertThat(actual,equalTo(expected));

  }

}
