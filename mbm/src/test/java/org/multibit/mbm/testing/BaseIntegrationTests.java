package org.multibit.mbm.testing;

import org.junit.Ignore;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 *  <p>Abstract base class to provide the following to database integration tests:</p>
 *  <ul>
 *  <li>Provides a one-off database load of standard objects shared between tests</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
@Ignore
public class BaseIntegrationTests extends AbstractTransactionalJUnit4SpringContextTests {

  //@Resource(name = "databaseLoader")
  //private DatabaseLoader databaseLoader;

}
