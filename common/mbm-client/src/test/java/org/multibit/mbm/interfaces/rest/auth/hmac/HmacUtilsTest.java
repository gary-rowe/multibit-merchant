package org.multibit.mbm.interfaces.rest.auth.hmac;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.client.impl.ClientRequestImpl;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.sun.jersey.core.impl.provider.entity.StringProvider;
import com.sun.jersey.spi.container.ContainerRequest;
import org.junit.Test;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HmacUtilsTest {

  private final String resourcePath = "/example/resource";
  private final String queryParameters = "?b=2&a=1";
  private final String baseUri = "http://example.org:8080";

  @Test
  public void testCanonicalRepresentation_Client_AllFields_Get() throws Exception {

    // Not required for GET but introduced for consistent style
    Providers providers = null;

    OutBoundHeaders headers = new OutBoundHeaders();
    headers.add(HttpHeaders.DATE, "Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST, "www.example.org");
    headers.add(HttpHeaders.USER_AGENT, "curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE, "Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE, "Thohn2Mohd2zugo");

    URI uri = new URI(baseUri + resourcePath + queryParameters);

    ClientRequest clientRequest = new ClientRequestImpl(uri, "get", null, headers);

    String representation = HmacUtils.createCanonicalRepresentation(clientRequest, providers);

    FixtureAsserts.assertStringMatchesStringFixture("GET canonical representation", representation, "/fixtures/hmac/expected-canonical-get.txt");

  }

  @Test
  public void testCanonicalRepresentation_Client_AllFields_Post() throws Exception {

    MessageBodyWriter<String> messageBodyWriter = new StringProvider();

    // Mock Providers
    Providers providers = mock(Providers.class);
    when(providers.getMessageBodyWriter(String.class, String.class,
      new Annotation[0], MediaType.APPLICATION_JSON_TYPE)).thenReturn(messageBodyWriter);

    OutBoundHeaders headers = new OutBoundHeaders();
    headers.add(HttpHeaders.DATE, "Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST, "www.example.org");
    headers.add(HttpHeaders.USER_AGENT, "curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE, "Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE, "Thohn2Mohd2zugo");

    URI uri = new URI(baseUri + resourcePath + queryParameters);

    String entity = "{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}";

    ClientRequest clientRequest = new ClientRequestImpl(uri, "post", entity, headers);

    String representation = HmacUtils.createCanonicalRepresentation(clientRequest, providers);

    FixtureAsserts.assertStringMatchesStringFixture("POST canonical representation", representation, "/fixtures/hmac/expected-canonical-post.txt");

  }

  @Test
  public void testCanonicalRepresentation_Server_AllFields_Get() throws Exception {

    // Mock headers
    InBoundHeaders headers = new InBoundHeaders();
    headers.add(HttpHeaders.DATE, "Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST, "www.example.org");
    headers.add(HttpHeaders.USER_AGENT, "curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE, "Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE, "Thohn2Mohd2zugo");

    // Mock request
    ContainerRequest containerRequest = mock(ContainerRequest.class);
    when(containerRequest.getRequestHeaders()).thenReturn(headers);
    when(containerRequest.getMethod()).thenReturn("GET");
    when(containerRequest.getRequestUri()).thenReturn(URI.create(baseUri + resourcePath + queryParameters));

    String representation = HmacUtils.createCanonicalRepresentation(containerRequest);

    FixtureAsserts.assertStringMatchesStringFixture("GET all fields canonical representation", representation, "/fixtures/hmac/expected-canonical-get.txt");

  }

  @Test
  public void testCanonicalRepresentation_Server_AllFields_Post() throws Exception {

    // Mock headers
    InBoundHeaders headers = new InBoundHeaders();
    headers.add(HttpHeaders.DATE, "Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST, "www.example.org");
    headers.add(HttpHeaders.USER_AGENT, "curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE, "Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE, "Thohn2Mohd2zugo");

    // Mock request
    ContainerRequest containerRequest = mock(ContainerRequest.class);
    when(containerRequest.getRequestHeaders()).thenReturn(headers);
    when(containerRequest.getMethod()).thenReturn("POST");
    when(containerRequest.getRequestUri()).thenReturn(URI.create(baseUri + resourcePath + queryParameters));

    // Simulate a user wrapped in HAL
    ByteArrayInputStream bais = new ByteArrayInputStream("{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}".getBytes());
    when(containerRequest.getEntityInputStream()).thenReturn(bais);

    String actualCanonicalRepresentation = HmacUtils.createCanonicalRepresentation(containerRequest);

    FixtureAsserts.assertStringMatchesStringFixture("POST all fields canonical representation", actualCanonicalRepresentation, "/fixtures/hmac/expected-canonical-post.txt");

  }


}
