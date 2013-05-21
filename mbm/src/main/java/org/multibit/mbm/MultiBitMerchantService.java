package org.multibit.mbm;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.multibit.mbm.auth.hmac.HmacServerAuthenticator;
import org.multibit.mbm.auth.hmac.HmacServerCredentials;
import org.multibit.mbm.auth.hmac.HmacServerRestrictedToProvider;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.health.TemplatePropertyHealthCheck;
import org.multibit.mbm.resources.BitcoinPaymentResource;
import org.multibit.mbm.resources.cart.AdminCartResource;
import org.multibit.mbm.resources.cart.PublicCartResource;
import org.multibit.mbm.resources.item.AdminItemResource;
import org.multibit.mbm.resources.item.PublicItemResource;
import org.multibit.mbm.resources.role.AdminRoleResource;
import org.multibit.mbm.resources.user.AdminUserResource;
import org.multibit.mbm.resources.user.ClientUserResource;
import org.multibit.mbm.resources.user.CustomerUserResource;
import org.multibit.mbm.resources.user.SupplierUserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class MultiBitMerchantService extends Service<MultiBitMerchantConfiguration> {

  public static final Logger log = LoggerFactory.getLogger(MultiBitMerchantService.class);

  /**
   * Main entry point to the application
   *
   * @param args Command line arguments
   *
   * @throws Exception If something goes wrong
   */
  public static void main(String[] args) throws Exception {
    new MultiBitMerchantService().run(args);
  }

  @Override
  public void initialize(Bootstrap<MultiBitMerchantConfiguration> bootstrap) {

    // No bundles required
  }

  @Override
  public void run(MultiBitMerchantConfiguration configuration, Environment environment) throws Exception {
    log.info("Reading configuration");

    // Read the configuration

    // Start Spring context based on the provided location
    // TODO Externalise this into the configuration - Spring provides too much to ignore
    ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
      "/spring/mbm-context.xml"
    });

    // Configure authenticator
    HmacServerAuthenticator hmacAuthenticator = context.getBean(HmacServerAuthenticator.class);
    CachingAuthenticator<HmacServerCredentials, User> cachingAuthenticator = CachingAuthenticator
      .wrap(hmacAuthenticator, CacheBuilderSpec.parse(configuration.getAuthenticationCachePolicy()));

    // Configure environment accordingly
    // Resources - admin (needs ROLE_ADMIN)
    environment.addResource(context.getBean(AdminUserResource.class));
    environment.addResource(context.getBean(AdminRoleResource.class));
    environment.addResource(context.getBean(AdminItemResource.class));
    environment.addResource(context.getBean(AdminCartResource.class));
    // Resources - client (needs ROLE_CLIENT)
    environment.addResource(context.getBean(ClientUserResource.class));
    // Resources - customer (needs ROLE_CUSTOMER)
    environment.addResource(context.getBean(CustomerUserResource.class));
    // Resources - supplier (needs ROLE_SUPPLIER)
    environment.addResource(context.getBean(SupplierUserResource.class));
    // Resources - public (no authentication)
    environment.addResource(context.getBean(BitcoinPaymentResource.class));
    environment.addResource(context.getBean(PublicCartResource.class));
    environment.addResource(context.getBean(PublicItemResource.class));

    // Health checks
    environment.addHealthCheck(new TemplatePropertyHealthCheck());

    // Providers
    environment.addProvider(new HmacServerRestrictedToProvider<User>(cachingAuthenticator, "REST"));

    // TODO Add the database loader code here
//    if (configuration.loadInitialData) {
//      new DataBaseLoader.initialise();
//    }
  }

}
