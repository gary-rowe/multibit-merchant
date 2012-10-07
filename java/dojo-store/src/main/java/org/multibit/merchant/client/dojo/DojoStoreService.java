package org.multibit.merchant.client.dojo;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import com.yammer.dropwizard.views.ViewMessageBodyWriter;
import org.multibit.merchant.client.dojo.health.DojoStoreHealthCheck;
import org.multibit.merchant.client.dojo.resources.RootResource;

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
    super("store");
  }

  @Override
  protected void initialize(DojoStoreConfiguration configuration,
                            Environment environment) {

    // Read the configuration

    // Configure authenticator

    // Start Spring context based on the provided location

    // Configure environment accordingly
    environment.addResource(new RootResource());

    // Health checks
    environment.addHealthCheck(new DojoStoreHealthCheck());

    // Providers
    environment.addProvider(new ViewMessageBodyWriter());

    // Bundles
    addBundle(new ViewBundle());

    // Create the asset bundle (tune the cache for development and production)
    CacheBuilderSpec cbs = CacheBuilderSpec.parse(configuration.getAssetCachePolicy());
    addBundle(new AssetsBundle("/assets/css", cbs, "/css"));
    addBundle(new AssetsBundle("/assets/js", cbs, "/js"));
    addBundle(new AssetsBundle("/assets/images", cbs, "/image"));
  }

}
