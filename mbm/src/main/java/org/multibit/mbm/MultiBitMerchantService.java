package org.multibit.mbm;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.config.Environment;
import org.multibit.mbm.auth.hmac.HmacAuthenticator;
import org.multibit.mbm.auth.hmac.HmacCredentials;
import org.multibit.mbm.auth.hmac.HmacRestrictedToProvider;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.health.TemplatePropertyHealthCheck;
import org.multibit.mbm.resources.BitcoinPaymentResource;
import org.multibit.mbm.resources.CustomerCartResource;
import org.multibit.mbm.resources.CustomerResource;
import org.multibit.mbm.resources.CustomerUserResource;
import org.multibit.mbm.resources.admin.AdminUserResource;

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

  /**
   * Main entry point to the application
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new MultiBitMerchantService().run(args);
  }

  private MultiBitMerchantService() {
    super("mbm");
  }

  @Override
  protected void initialize(MultiBitMerchantConfiguration configuration,
                            Environment environment) {

    // Read the configuration
    final String template = configuration.getTemplate();
    final String defaultName = configuration.getDefaultName();

    // Configure authenticator
    Authenticator<HmacCredentials, User> authenticator = new HmacAuthenticator();
    CachingAuthenticator<HmacCredentials, User> cachingAuthenticator = CachingAuthenticator.wrap(authenticator, CacheBuilderSpec.parse(configuration.getAuthenticationCachePolicy()));

    // Configure environment accordingly
    // Resources - admin
    environment.addResource(new AdminUserResource());
    // Resource - other
    environment.addResource(new CustomerResource());
    environment.addResource(new CustomerCartResource());
    environment.addResource(new CustomerUserResource());
    environment.addResource(new BitcoinPaymentResource());

    // Health checks
    environment.addHealthCheck(new TemplatePropertyHealthCheck(template));

    // Providers
    environment.addProvider(new HmacRestrictedToProvider<User>(cachingAuthenticator, "REST"));
  }


}
