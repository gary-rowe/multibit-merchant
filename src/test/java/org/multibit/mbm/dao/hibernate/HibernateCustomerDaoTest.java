package org.multibit.mbm.dao.hibernate;

import org.junit.Test;
import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.domain.Customer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = {"/spring/test-mbm-hibernate-dao.xml"})
public class HibernateCustomerDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Resource(name= "hibernateCustomerDao")
  CustomerDao testObject;

  @Test
  public void testNewCustomer() {

    UUID uuid=UUID.randomUUID();
    String openId="abc123";
    Customer customer = new Customer(uuid, openId);

    int originalRows = countRowsInTable("customers");

    testObject.newCustomer(customer);

    int updatedRows = countRowsInTable("customers");

    assertThat(updatedRows, equalTo(originalRows+1));
  }
}
