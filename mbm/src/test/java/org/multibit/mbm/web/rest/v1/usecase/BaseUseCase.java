package org.multibit.mbm.web.rest.v1.usecase;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *  <p>Abstract base class to provide the following to {@link UseCase}:</p>
 *  <ul>
 *  <li>Provision of standard methods</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public abstract class BaseUseCase implements UseCase {

  public static final String HOST = "localhost";
  public static final int PORT = 8080;

  public static final String API_PREFIX = String.format("http://%s:%d/mbm/api/v1", HOST, PORT);

  /**
   * @param useCaseParameterMap The shared state map
   *
   * @return A configured RestTemplate with appropriate HTTP headers (cookies, authentication etc)
   */
  private RestTemplate getConfiguredRestTemplate(Map<UseCaseParameter, Object> useCaseParameterMap) {


    HttpHost targetHost = new HttpHost(HOST, PORT, "http");

    DefaultHttpClient httpClient = new DefaultHttpClient();

    // Set accept headers

    // Check for authentication
    if (useCaseParameterMap.containsKey(UseCaseParameter.HTTP_AUTHENTICATE_BASIC))
    httpClient.getCredentialsProvider().setCredentials(
      new AuthScope(targetHost.getHostName(), targetHost.getPort()),
      new UsernamePasswordCredentials("username", "password"));

    // Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();

    // Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();
    authCache.put(targetHost, basicAuth);

    // Add AuthCache to the execution context
    BasicHttpContext httpContext = new BasicHttpContext();
    httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);

    // Use the HTTP client for REST requests
    HttpComponentsClientHttpRequestFactory commons = new HttpComponentsClientHttpRequestFactory(httpClient);

    RestTemplate restTemplate = new RestTemplate(commons);

    return restTemplate;
  }

  /**
   * @param resource The resource (e.g. "catalog/1/item/52"
   *
   * @return The full URI (e.g. "http://localhost:8080/mbm/api/v1/catalog/1/item/52")
   */
  protected String buildResourceUri(String resource) {
    return API_PREFIX + resource;
  }
  
  public final void execute(Map<UseCaseParameter, Object> useCaseParameterMap) {
    RestTemplate restTemplate=getConfiguredRestTemplate(useCaseParameterMap);
    doExecute(useCaseParameterMap, null);
  }

  /**
   * Use case specific implementation details (not generally visible)
   * @param useCaseParameterMap The shared parameter map
   * @param restTemplate A suitably configured {@link RestTemplate}
   */
  protected abstract void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate);

}
