package org.multibit.merchant.client.store;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import com.yammer.dropwizard.views.ViewMessageBodyWriter;
import org.multibit.merchant.client.store.health.StoreHealthCheck;
import org.multibit.merchant.client.store.resources.PublicHomeResource;

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
public class StoreService extends Service<StoreConfiguration> {

  /**
   * Main entry point to the application
   *
   * @param args CLI arguments
   *
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new StoreService().run(args);
  }

  private StoreService() {
    super("store");
    // TODO Work out why this has to be in the constructor or it fails
    CacheBuilderSpec cacheBuilderSpec = (System.getenv("FILE_CACHE_ENABLED") == null) ? CacheBuilderSpec.parse("maximumSize=0") : AssetsBundle.DEFAULT_CACHE_SPEC;
    addBundle(new AssetsBundle("/assets/css", cacheBuilderSpec, "/css"));
    addBundle(new AssetsBundle("/assets/js", cacheBuilderSpec, "/js"));
    addBundle(new AssetsBundle("/assets/images", cacheBuilderSpec, "/images"));
  }

  @Override
  protected void initialize(StoreConfiguration configuration,
                            Environment environment) {

    // Read the configuration

    // Configure authenticator

    // Start Spring context based on the provided location

    // Configure environment accordingly
    environment.scanPackagesForResourcesAndProviders(PublicHomeResource.class);

    // Health checks
    environment.addHealthCheck(new StoreHealthCheck());

    // Providers
    environment.addProvider(new ViewMessageBodyWriter());

    // Bundles
    addBundle(new ViewBundle());

  }

}
