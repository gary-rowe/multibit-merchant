package org.multibit.mbm.test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.yammer.dropwizard.bundles.JavaBundle;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.jersey.OptionalQueryParamInjectableProvider;
import com.yammer.dropwizard.json.Json;
import org.codehaus.jackson.map.Module;
import org.junit.After;
import org.junit.Before;
import org.multibit.mbm.auth.hmac.HmacClientFilter;
import org.multibit.mbm.auth.hmac.HmacServerAuthenticator;
import org.multibit.mbm.auth.hmac.HmacServerRestrictedToProvider;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A base test class for testing Dropwizard resources against
 * an actual Jersey container using HMAC for authentication
 */
public abstract class BaseJerseyHmacResourceTest extends BaseResourceTest {
  private final Set<Object> singletons = Sets.newHashSet();
  private final Set<Class<?>> providers = Sets.newHashSet();
  private final List<Module> modules = Lists.newArrayList();
  private final Map<String, Boolean> features = Maps.newHashMap();

  private JerseyTest test;
  protected UriInfo uriInfo;

  /**
   * The User providing authentication via HMAC
   */
  protected User hmacUser;

  /**
   * <p>Subclasses must use this to configure mocks of any objects that the
   * test object depends on.</p>
   *
   * @throws Exception If something goes wrong
   */
  protected abstract void setUpResources() throws Exception;

  protected void addSingleton(Object singleton) {
    singletons.add(singleton);
  }

  public void addProvider(Class<?> klass) {
    providers.add(klass);
  }

  protected void addJacksonModule(Module module) {
    modules.add(module);
  }

  protected void addFeature(String feature, Boolean value) {
    features.put(feature, value);
  }

  protected Json getJson() {
    final Json json = new Json();
    for (Module module : modules) {
      json.registerModule(module);
    }
    return json;
  }

  protected Client client() {
    return test.client();
  }

  @Before
  public void setUpJersey() throws Exception {

    // Provide any specialised resource configuration
    setUpResources();

    this.test = new JerseyTest() {
      @Override
      protected AppDescriptor configure() {
        final DropwizardResourceConfig config = new DropwizardResourceConfig(true);
        // Default singletons
        for (Object provider : JavaBundle.DEFAULT_PROVIDERS) {
          config.getSingletons().add(provider);
        }
        // Configure Jackson
        final Json json = getJson();
        config.getSingletons().add(new JacksonMessageBodyProvider(json));
        config.getSingletons().addAll(singletons);

        // Add providers
        for (Class<?> provider : providers) {
          config.getClasses().add(provider);
        }
        config.getClasses().add(OptionalQueryParamInjectableProvider.class);

        // Add any features (see FeaturesAndProperties)
        for (Map.Entry<String, Boolean> feature : features.entrySet()) {
          config.getFeatures().put(feature.getKey(), feature.getValue());
        }

        return new LowLevelAppDescriptor.Builder(config).build();
      }
    };

    // Allow final client request filtering for HMAC authentication (this allows for secure tests)
    test.client().addFilter(new HmacClientFilter(
      test.client().getProviders()
    )
    );

    test.setUp();

    // Configure for weak hashes
    UserBuilder.useWeakDigestOnly =true;
  }

  @After
  public void tearDownJersey() throws Exception {
    if (test != null) {
      test.tearDown();
    }
  }

  /**
   * Provides the default client HMAC authentication settings (not a Customer)
   */
  protected User setUpClientHmacAuthenticator() {

    Role clientRole = DatabaseLoader.buildClientRole();
    User storeClient = DatabaseLoader.buildStoreClient(clientRole);

    return setUpHmacAuthenticator(storeClient);
  }

  /**
   * Provides HMAC authentication settings for Alice Customer
   */
  protected User setUpAliceHmacAuthenticator() {

    Role customerRole = DatabaseLoader.buildCustomerRole();
    User aliceCustomer = DatabaseLoader.buildAliceCustomer(customerRole);

    return setUpHmacAuthenticator(aliceCustomer);
  }

  /**
   * Provides HMAC authentication settings for anonymous Public user
   */
  protected User setUpPublicHmacAuthenticator() {

    Role publicRole = DatabaseLoader.buildPublicRole();
    User anonymousPublic = DatabaseLoader.buildAnonymousPublic(publicRole);

    return setUpHmacAuthenticator(anonymousPublic);
  }

  /**
   * Provides HMAC authentication settings for Trent Admin
   */
  protected User setUpTrentHmacAuthenticator() {

    Role adminRole = DatabaseLoader.buildAdminRole();
    User trentAdmin = DatabaseLoader.buildTrentAdministrator(adminRole);

    return setUpHmacAuthenticator(trentAdmin);
  }

  /**
   * @param user The User to act as the authenticator for HMAC communications (could be a Customer)
   */
  protected User setUpHmacAuthenticator(User user) {

    UserDao userDao = mock(UserDao.class);
    when(userDao.getByApiKey(user.getApiKey())).thenReturn(Optional.of(user));

    HmacServerAuthenticator authenticator = new HmacServerAuthenticator();
    authenticator.setUserDao(userDao);

    addSingleton(new HmacServerRestrictedToProvider<User>(authenticator, "REST"));

    // Set the HMAC authentication user
    hmacUser = user;

    return user;
  }

  /**
   * Configure request as a client to access the resource on behalf of a user
   *
   * @param path The relative path to the resource
   *
   * @return A web resource suitable for method chaining
   *
   * TODO Replace the path with the URIBuilder .class approach
   */
  protected WebResource configureAsClient(String path) {
    WebResource resource = client().resource(path);
    resource.setProperty(HmacClientFilter.MBM_API_KEY, hmacUser.getApiKey());
    resource.setProperty(HmacClientFilter.MBM_SECRET_KEY, hmacUser.getSecretKey());
    return resource;
  }
}