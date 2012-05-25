package org.multibit.mbm;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Environment;
import org.multibit.mbm.health.TemplatePropertyHealthCheck;
import org.multibit.mbm.resources.HelloWorldResource;
import org.multibit.mbm.security.SimpleOAuth2Authenticator;
import org.multibit.mbm.security.SimpleUser;

/**
 * <p>Service to provide the following to application:</p>
 * <ul>
 * <li>Provision of access to resources</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class MultiBitMerchantService extends Service<MultiBitMerchantConfiguration> {

  public static void main(String[] args) throws Exception {
    new MultiBitMerchantService().run(args);
  }

  private MultiBitMerchantService() {
    super("multibit-merchant");
  }

  @Override
  protected void initialize(MultiBitMerchantConfiguration configuration,
                            Environment environment) {

    // Read the configuration
    final String template = configuration.getTemplate();
    final String defaultName = configuration.getDefaultName();

    // Configure authenticator
    Authenticator<String, SimpleUser> authenticator = new SimpleOAuth2Authenticator();
    CachingAuthenticator<String, SimpleUser> cachingAuthenticator = CachingAuthenticator.wrap(authenticator, CacheBuilderSpec.parse(configuration.getAuthenticationCachePolicy()));

    // Configure environment accordingly
    // Resources
    environment.addResource(new HelloWorldResource(template, defaultName));
    // Health checks
    environment.addHealthCheck(new TemplatePropertyHealthCheck(template));
    // Providers
    environment.addProvider(new OAuthProvider<SimpleUser>(cachingAuthenticator,"Secured realm"));}

}
