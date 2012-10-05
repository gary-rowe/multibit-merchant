package org.multibit.merchant.client.dojo;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;

/**
 * <p>Service to provide the following to application:</p>
 * <ul>
 * <li>Provision of access to resources</li>
 * </ul>
 * <p>Use <code>java -jar mbm-develop-SNAPSHOT.jar server mbm.yml</code> to start MBM</p>
 *
 * @since 0.0.1
 *         
 */
public class DojoStoreService extends Service<DojoStoreConfiguration> {

  /**
   * Main entry point to the application
   *
   * @param args
   *
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new DojoStoreService().run(args);
  }

  private DojoStoreService() {
    super("mbm");
  }

  @Override
  protected void initialize(DojoStoreConfiguration configuration,
                            Environment environment) {

    // Read the configuration

    // Configure authenticator

    // Start Spring context based on the provided location

    // Configure environment accordingly

    // Health checks

    // Providers

  }


}
