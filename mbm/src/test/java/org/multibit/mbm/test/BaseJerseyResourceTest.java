package org.multibit.mbm.test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.net.HttpHeaders;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.yammer.dropwizard.bundles.JavaBundle;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.json.Json;
import junit.framework.AssertionFailedError;
import org.codehaus.jackson.map.Module;
import org.junit.After;
import org.junit.Before;
import org.multibit.mbm.auth.hmac.HmacAuthenticator;
import org.multibit.mbm.auth.hmac.HmacClientFilter;
import org.multibit.mbm.auth.hmac.HmacRestrictedToProvider;
import org.multibit.mbm.auth.hmac.HmacUtils;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.*;

import javax.ws.rs.core.UriInfo;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    // Allow final request filtering for HMAC authentication
    test.client().addFilter(new HmacClientFilter(apiKey.get(), sharedSecret.get()));
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
  protected void setUpAuthenticator() {
    setUpAuthenticator(apiKey, sharedSecret);
  }

  /**
   * @param apiKey    The API key to assign to the User (default is "abc123")
   * @param sharedSecret The shared secret to assign (default is "def456")
   */
  protected void setUpAuthenticator(Optional<String> apiKey, Optional<String> sharedSecret) {

    // TODO Consider a shortcut for this in UserBuilder (asCustomer())
    Role customerRole = RoleBuilder.getInstance()
      .setName(Authority.ROLE_CUSTOMER.name())
      .setDescription("Customer role")
      .addCustomerAuthorities()
      .build();

    User user = UserBuilder
      .getInstance()
      .setUUID(apiKey.get())
      .setSecretKey(sharedSecret.get())
      .addRole(customerRole)
      .build();

    UserDao userDao = mock(UserDao.class);
    when(userDao.getUserByUUID(apiKey.get())).thenReturn(user);

    HmacAuthenticator authenticator = new HmacAuthenticator();
    authenticator.setUserDao(userDao);

    addProvider(new HmacRestrictedToProvider<User>(authenticator, "REST"));

  }

  /**
   * Inserts an appropriate HTTP Authorization header for the MBM authentication process
   *
   * @param method      The HTTP method that will be used
   * @param webResource The fully constructed Jersey web resource prior to
   *
   * @return The web resource on which the client call can be made (e.g. .get(), .post() etc)
   */
  protected WebResource.Builder authorize(String method, WebResource webResource) {
    String canonicalRepresentation = method + apiKey.get() + sharedSecret.get();

    String hmacSignature;
    try {
      hmacSignature = new String(HmacUtils.computeSignature(
        "HmacSHA1",
        canonicalRepresentation.getBytes(),
        sharedSecret.get().getBytes()));

    } catch (NoSuchAlgorithmException e) {
      throw new AssertionFailedError(e.getMessage());
    } catch (InvalidKeyException e) {
      throw new AssertionFailedError(e.getMessage());
    }

    String authorization = String.format("HMAC %s %s", apiKey.get(), hmacSignature);

    return webResource.header(HttpHeaders.AUTHORIZATION, authorization);


  }

  /**
   * Create a canonical representation of the HTTP request suitable for signing
   *
   * @param method      The HTTP method
   * @param resource The web resource containing the information required
   */
  private String createCanonicalRepresentation(String method, WebResource resource) {

    StringBuilder canonicalRepresentation = new StringBuilder("Hello!");

    return canonicalRepresentation.toString();

//    // Provide a map of all header names converted to lowercase
//    MultivaluedMap<String, String> headers = resource.getRequestBuilder().head();
//    // Create a lexicographically sorted set of the header names for lookup later
//    Set<String> headerNames = Sets.newTreeSet(headers.keySet());
//    // Remove some headers that should not be included or will have special treatment
//    headerNames.remove(javax.ws.rs.core.HttpHeaders.DATE);
//    headerNames.remove(HmacUtils.X_HMAC_NONCE);
//    // TODO Check if the following should be removed (would the client know them in advance?)
//    headerNames.remove(javax.ws.rs.core.HttpHeaders.USER_AGENT);
//    headerNames.remove(javax.ws.rs.core.HttpHeaders.HOST);
//
//    // Start with the empty string ("")
//    final StringBuilder canonicalRepresentation = new StringBuilder("");
//
//    // Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).
//    canonicalRepresentation
//      .append(httpContext.getRequest().getMethod().toUpperCase())
//      .append("\n");
//
//    // Add the date for the request using the form "date:#date-of-request" followed by a single newline. The date for the signature must be formatted exactly as in the request.
//    canonicalRepresentation
//      .append("date:")
//      .append(headers.getFirst(javax.ws.rs.core.HttpHeaders.DATE))
//      .append("\n");
//
//    // Add the nonce for the request in the form "nonce:#nonce-in-request" followed by a single newline. If no nonce is passed use the empty string as nonce value.
//    if (headers.containsKey(HmacUtils.X_HMAC_NONCE)) {
//      canonicalRepresentation
//        .append("nonce:")
//        .append(headers.getFirst(HmacUtils.X_HMAC_NONCE))
//        .append("\n");
//    }
//
//    // Sort the remaining headers lexicographically by header name.
//    // Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.
//    // Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.
//    // Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.
//    for (String headerName : headerNames) {
//      canonicalRepresentation
//        .append(headerName.toLowerCase())
//        .append(":");
//      // TODO Consider effect of different separators on this list
//      for (String value : headers.get(headerName)) {
//        canonicalRepresentation
//          .append(value);
//      }
//      canonicalRepresentation
//        .append("\n");
//    }
//
//    // Append the url-decoded query path to the canonical representation
//    MultivaluedMap<String, String> decodedQueryParameters = httpContext.getRequest().getQueryParameters();
//    if (!decodedQueryParameters.isEmpty()) {
//      canonicalRepresentation.append("?");
//      for (Map.Entry<String, List<String>> queryParameter : decodedQueryParameters.entrySet()) {
//        canonicalRepresentation
//          .append(queryParameter.getKey())
//          .append("&");
//        // TODO Consider effect of different separators on this list
//        for (String value : queryParameter.getValue()) {
//          canonicalRepresentation
//            .append(value);
//        }
//      }
//    }
//
//    return canonicalRepresentation.toString();
//  }

}

}