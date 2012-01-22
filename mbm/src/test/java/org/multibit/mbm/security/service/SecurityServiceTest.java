package org.multibit.mbm.security.service;

import org.junit.Test;
import org.multibit.mbm.security.dao.UserDao;
import org.multibit.mbm.security.dto.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertNotNull;

public class SecurityServiceTest {

  /**
   * Verifies the transaction configuration by Spring
   */
  @Test
  public void testHaveBeenAuthenticated_SpringTx() {

    // Configure supporting objects

    // This context will pull in the UserService using the production TransactionManager
    ApplicationContext context = new ClassPathXmlApplicationContext("/spring/test-mbm-context.xml");

    SecurityService testObject = (SecurityService) context.getBean("securityService");

    // Perform the test
    testObject.persistAuthenticatedUser("new123");

    UserDao userDao = testObject.getUserDao();
    User user = userDao.getUserByOpenId("new123");
    
    assertNotNull(user);

  }
}
