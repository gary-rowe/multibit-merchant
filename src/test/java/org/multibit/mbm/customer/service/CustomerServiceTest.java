package org.multibit.mbm.customer.service;

import org.junit.Test;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.customer.dto.Customer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertNotNull;

public class CustomerServiceTest {

  /**
   * Verifies the transaction configuration by Spring
   */
  @Test
  public void testHaveBeenAuthenticated_SpringTx() {

    // Configure supporting objects

    // This context will pull in the CustomerService using the production TransactionManager
    ApplicationContext context = new ClassPathXmlApplicationContext("/spring/test-mbm-context.xml");

    CustomerService testObject = (CustomerService) context.getBean("customerService");

    // Perform the test
    testObject.persistAuthenticatedCustomer("abc123");

    CustomerDao customerDao = testObject.getCustomerDao();
    Customer customer = customerDao.getCustomerByOpenId("abc123");
    
    assertNotNull(customer);

  }
}
