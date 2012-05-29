package org.multibit.mbm.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.xeiam.xchange.utils.CryptoUtils;
import com.yammer.dropwizard.bundles.JavaBundle;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.json.Json;
import org.codehaus.jackson.map.Module;
import org.junit.After;
import org.junit.Before;
import org.multibit.mbm.auth.hmac.HmacAuthProvider;
import org.multibit.mbm.auth.hmac.HmacAuthenticator;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A base test class for testing Dropwizard resources.
 */
public abstract class BaseResourceTest {
  private final Set<Object> singletons = Sets.newHashSet();
  private final Set<Object> providers = Sets.newHashSet();
  private final List<Module> modules = Lists.newArrayList();

  private JerseyTest test;

  protected abstract void setUpResources() throws Exception;

  protected void addResource(Object resource) {
    singletons.add(resource);
  }

  public void addProvider(Object provider) {
    providers.add(provider);
  }

  protected void addJacksonModule(Module module) {
    modules.add(module);
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
        for (Object provider : JavaBundle.DEFAULT_PROVIDERS) { // sorry, Scala folks
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
    test.setUp();
  }

  @After
  public void tearDownJersey() throws Exception {
    if (test != null) {
      test.tearDown();
    }
  }

  /**
   * @param contents The content to sign with the default HMAC process (POST body, GET resource path)
   * @return
   */
  protected String buildHmacAuthorization(String contents, String apiKey, String secretKey) throws UnsupportedEncodingException, GeneralSecurityException {
    return String.format("HmacSHA1 %s %s",apiKey, CryptoUtils.computeSignature("HmacSHA1", contents, secretKey));
  }

  protected void setUpAuthenticator() {
    User user = UserBuilder
      .getInstance()
      .setUUID("abc123")
      .setSecretKey("def456")
      .build();

    //
    UserDao userDao = mock(UserDao.class);
    when(userDao.getUserByUUID("abc123")).thenReturn(user);

    HmacAuthenticator authenticator = new HmacAuthenticator();
    authenticator.setUserDao(userDao);

    addProvider(new HmacAuthProvider<User>(authenticator, "REST"));
  }
}