package org.multibit.mbm.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.auth.hmac.HmacClientFilter;
import org.multibit.mbm.model.ClientUser;

import javax.ws.rs.core.HttpHeaders;
import java.net.URI;
import java.util.Locale;

/**
 * <p>Global singleton factory to provide the following to upstream client:</p>
 * <ul>
 * <li>Provisioning Jersey client resources using HMAC authentication</li>
 * <li>Configuration for accepting HAL with a given Locale</li>
 * <li>Configuration for providing specific user details or acting as a client (anonymous user)</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public enum HalHmacResourceFactory {

  INSTANCE;

  private Client client = null;
  private String clientApiKey = null;
  private String clientSecretKey = null;
  private URI baseUri;

  /**
   * @param client The Jersey client to use for building all upstream requests
   */
  public HalHmacResourceFactory setJerseyClient(Client client) {
    this.client = client;
    return this;
  }

  /**
   * @param clientApiKey The client API key used in the absence of a user request
   */
  public HalHmacResourceFactory setClientApiKey(String clientApiKey) {
    this.clientApiKey = clientApiKey;
    return this;
  }

  /**
   * @param clientSecretKey The client secret key used in the absence of a user request
   */
  public HalHmacResourceFactory setClientSecretKey(String clientSecretKey) {
    this.clientSecretKey = clientSecretKey;
    return this;
  }

  /**
   * @param baseUri The base URI of the upstream server
   */
  public HalHmacResourceFactory setBaseUri(URI baseUri) {
    this.baseUri = baseUri;
    return this;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public WebResource newUserResource(Locale locale, String path, ClientUser clientUser) {

    // Configure the URI
    URI uri = URI.create(getBaseUri() + path);

    // Configure the resource
    WebResource resource = client.resource(uri);
    resource.setProperty(HmacClientFilter.MBM_API_KEY, clientUser.getApiKey());
    resource.setProperty(HmacClientFilter.MBM_SECRET_KEY, clientUser.getSecretKey());
    resource.accept(HalMediaType.APPLICATION_HAL_JSON);
    resource.header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString());

    // Provide further access to the request builder to allow chaining
    return resource;
  }

  public WebResource newClientResource(Locale locale, String path) {

    // Configure the URI
    URI uri = URI.create(getBaseUri() + path);

    // Configure the resource
    WebResource resource = client.resource(uri);
    resource.setProperty(HmacClientFilter.MBM_API_KEY, clientApiKey);
    resource.setProperty(HmacClientFilter.MBM_SECRET_KEY, clientSecretKey);
    resource.accept(HalMediaType.APPLICATION_HAL_JSON);
    resource.header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString());

    // Provide further access to the request builder to allow chaining
    return resource;

  }

}
