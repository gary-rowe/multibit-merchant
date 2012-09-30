package org.multibit.mbm.test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.yammer.dropwizard.bundles.JavaBundle;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.json.Json;
import org.codehaus.jackson.map.Module;
import org.junit.After;
import org.junit.Before;
import org.multibit.mbm.auth.hmac.HmacAuthenticator;
import org.multibit.mbm.auth.hmac.HmacClientFilter;
import org.multibit.mbm.auth.hmac.HmacRestrictedToProvider;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.*;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A base test class for testing Dropwizard resources against
 * an actual Jersey container
 */
public abstract class BaseJerseyResourceTest extends BaseResourceTest {
  private final Set<Object> singletons = Sets.newHashSet();
  private final Set<Object> providers = Sets.newHashSet();
  private final List<Module> modules = Lists.newArrayList();

  private JerseyTest test;
  protected UriInfo uriInfo;

  private Optional<String> apiKey = Optional.of("abc123");
  private Optional<String> sharedSecret = Optional.of("def456");

  /**
   * <p>Subclasses must use this to configure mocks of any objects that the
   * test object depends on.</p>
   *
   * @throws Exception If something goes wrong
   */
  protected abstract void setUpResources() throws Exception;

  protected void addResource(Object resource) {
    singletons.add(resource);
  }

  public void addProvider(Object provider) {
    providers.add(provider);
  }

  protected Json getJson() {
    return new Json();
  }

  protected Client client() {
    return test.client();
  }

  @Before
  public void setUpJersey() throws Exception {
    setUpResources();
    this.test = new JerseyTest() {
      @Override
      protected AppDescriptor configure() {
        final DropwizardResourceConfig config = new DropwizardResourceConfig();

        for (Object provider : JavaBundle.DEFAULT_PROVIDERS) {
          config.getSingletons().add(provider);
        }

        for (Object provider : providers) {
          config.getSingletons().add(provider);
        }

        Json json = getJson();
        for (Module module : modules) {
          json.registerModule(module);
        }

        config.getSingletons().add(new JacksonMessageBodyProvider(json));
        config.getSingletons().addAll(singletons);

        return new LowLevelAppDescriptor.Builder(config).build();
      }

    };

    // Allow final client request filtering for HMAC authentication (this allows for secure tests)
    test.client().addFilter(new HmacClientFilter(apiKey.get(), sharedSecret.get(), test.client().getProviders()));

    test.setUp();

  }

  @After
  public void tearDownJersey() throws Exception {
    if (test != null) {
      test.tearDown();
    }
  }

  /**
   * Provides the default authentication settings
   */
  protected User setUpAuthenticator() {

    Role customerRole = DatabaseLoader.buildCustomerRole();

    return setUpAuthenticator(apiKey, sharedSecret, Lists.newArrayList(customerRole));
  }

  /**
   * Provides the default authentication settings
   */
  protected User setUpAuthenticator(List<Role> roles) {
    return setUpAuthenticator(apiKey, sharedSecret, roles);
  }

  /**
   * @param apiKey    The API key to assign to the User (default is "abc123")
   * @param sharedSecret The shared secret to assign (default is "def456")
   */
  protected User setUpAuthenticator(Optional<String> apiKey, Optional<String> sharedSecret, List<Role> roles) {

    Customer customer = CustomerBuilder.newInstance()
      .build();

    User user = UserBuilder
      .newInstance()
      .withUUID(apiKey.get())
      .withSecretKey(sharedSecret.get())
      .withRoles(roles)
      .withCustomer(customer)
      .build();

    UserDao userDao = mock(UserDao.class);
    when(userDao.getUserByUUID(apiKey.get())).thenReturn(user);

    HmacAuthenticator authenticator = new HmacAuthenticator();
    authenticator.setUserDao(userDao);

    addProvider(new HmacRestrictedToProvider<User>(authenticator, "REST"));

    return user;
  }

}