package org.multibit.mbm.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.jersey.OptionalQueryParamInjectableProvider;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import com.yammer.dropwizard.validation.Validator;
import org.codehaus.jackson.map.Module;
import org.junit.After;
import org.junit.Before;
import org.multibit.mbm.interfaces.rest.auth.hmac.HmacClientFilter;
import org.multibit.mbm.interfaces.rest.auth.hmac.HmacServerAuthenticator;
import org.multibit.mbm.interfaces.rest.auth.hmac.HmacServerRestrictedToProvider;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.model.model.UserBuilder;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.repositories.UserReadService;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
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
  private final ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();
  private final Map<String, Boolean> features = Maps.newHashMap();
  private final Map<String, Object> properties = Maps.newHashMap();
  private final List<Module> modules = Lists.newArrayList();

  private JerseyTest test;
  private Validator validator = new Validator();

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

  public Validator getValidator() {
    return validator;
  }

  public void setValidator(Validator validator) {
    this.validator = validator;
  }

  protected void addResource(Object resource) {
    singletons.add(resource);
  }

  public void addProvider(Class<?> klass) {
    providers.add(klass);
  }

  public void addProvider(Object provider) {
    singletons.add(provider);
  }

  protected ObjectMapperFactory getObjectMapperFactory() {
    return objectMapperFactory;
  }

  protected void addFeature(String feature, Boolean value) {
    features.put(feature, value);
  }

  protected void addProperty(String property, Object value) {
    properties.put(property, value);
  }

  protected Client client() {
    return test.client();
  }

  protected JerseyTest getJerseyTest() {
    return test;
  }

  @Before
  public void setUpJersey() throws Exception {

    // Provide any specialised resource configuration
    setUpResources();

//    this.test = new JerseyTest() {
//
//      @Override
//      protected URI getBaseURI() {
//        return URI.create("http://localhost:8080/");
//      }
//
//      @Override
//      protected AppDescriptor configure() {
//        final DropwizardResourceConfig config = new DropwizardResourceConfig(true);
//        // Default singletons
//        for (Object provider : JavaBundle.DEFAULT_PROVIDERS) {
//          config.getSingletons().add(provider);
//        }
//        // Configure Jackson
//        final Json json = getJson();
//        config.getSingletons().add(new JacksonMessageBodyProvider(json));
//        config.getSingletons().addAll(singletons);
//
//        // Add providers
//        for (Class<?> provider : providers) {
//          config.getClasses().add(provider);
//        }
//        config.getClasses().add(OptionalQueryParamInjectableProvider.class);
//
//        // Add any features (see FeaturesAndProperties)
//        for (Map.Entry<String, Boolean> feature : features.entrySet()) {
//          config.getFeatures().put(feature.getKey(), feature.getValue());
//        }
//
//        return new LowLevelAppDescriptor
//          .Builder(config)
//          .build();
//      }
//    };

    this.test = new JerseyTest() {

      @Override
      protected URI getBaseURI() {
        return URI.create("http://localhost:8080/");
      }


      @Override
      protected AppDescriptor configure() {
        final DropwizardResourceConfig config = new DropwizardResourceConfig(true);

        for (Class<?> provider : providers) {
          config.getClasses().add(provider);
        }
        config.getClasses().add(OptionalQueryParamInjectableProvider.class);

        for (Map.Entry<String, Boolean> feature : features.entrySet()) {
          config.getFeatures().put(feature.getKey(), feature.getValue());
        }

        // Add any features (see FeaturesAndProperties)
        for (Map.Entry<String, Object> property : properties.entrySet()) {
          config.getProperties().put(property.getKey(), property.getValue());
        }

        // Configure Jackson
        final ObjectMapper mapper = getObjectMapperFactory().build();
        config.getSingletons().add(new JacksonMessageBodyProvider(mapper, validator));
        config.getSingletons().addAll(singletons);

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
    UserBuilder.isTestMode =true;
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
   * Provides HMAC authentication settings for Steve Supplier
   */
  protected User setUpSteveHmacAuthenticator() {

    Role supplierRole = DatabaseLoader.buildSupplierRole();
    User steveSupplier = DatabaseLoader.buildSteveSupplier(supplierRole);

    return setUpHmacAuthenticator(steveSupplier);
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

    UserReadService userReadService = mock(UserReadService.class);
    when(userReadService.getByApiKey(user.getApiKey())).thenReturn(Optional.of(user));

    HmacServerAuthenticator authenticator = new HmacServerAuthenticator();
    authenticator.setUserReadService(userReadService);

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
   */
  protected WebResource configureAsClient(String path) {
    WebResource resource = client().resource(path);
    resource.setProperty(HmacClientFilter.MBM_API_KEY, hmacUser.getApiKey());
    resource.setProperty(HmacClientFilter.MBM_SECRET_KEY, hmacUser.getSecretKey());
    return resource;
  }

  /**
   * Configure request as a client to access the resource on behalf of a user
   *
   * @param clazz The class of the Resource
   *
   * @return A web resource suitable for method chaining
   *
   */
  protected WebResource configureAsClient(Class clazz) {
    URI uri = UriBuilder
      .fromResource(clazz)
      .scheme("http")
      .host("localhost")
      .port(8080)
      .build();
    WebResource resource = client().resource(uri);
    resource.setProperty(HmacClientFilter.MBM_API_KEY, hmacUser.getApiKey());
    resource.setProperty(HmacClientFilter.MBM_SECRET_KEY, hmacUser.getSecretKey());
    return resource;
  }

}