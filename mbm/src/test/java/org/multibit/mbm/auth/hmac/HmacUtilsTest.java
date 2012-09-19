package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Lists;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.client.impl.ClientRequestImpl;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.sun.jersey.core.impl.provider.entity.StringProvider;
import com.sun.jersey.spi.container.ContainerRequest;
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider;
import com.yammer.dropwizard.json.Json;
import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HmacUtilsTest {
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

    URI uri = new URI("http://example.org/example/resource.html?sort=header%20footer&order=ASC");

    ClientRequest clientRequest = new ClientRequestImpl(uri, "get", null, headers);

    String actualCanonicalRepresentation = HmacUtils.createCanonicalRepresentation(clientRequest, providers);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-get.txt");

    assertThat(actualCanonicalRepresentation, equalTo(expectedCanonicalRepresentation));

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

    URI uri = new URI("http://example.org/example/resource.html?sort=header%20footer&order=ASC");

    String entity = "{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}";

    ClientRequest clientRequest = new ClientRequestImpl(uri, "post", entity, headers);

    String actualCanonicalRepresentation = HmacUtils.createCanonicalRepresentation(clientRequest, providers);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-post.txt");

    assertThat(actualCanonicalRepresentation, equalTo(expectedCanonicalRepresentation));

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

    // Mock query parameters
    InBoundHeaders queryParameters = new InBoundHeaders();
    queryParameters.put("sort", Lists.newArrayList("header footer"));
    queryParameters.put("order", Lists.newArrayList("ASC"));

    // Mock request
    ContainerRequest containerRequest = mock(ContainerRequest.class);
    when(containerRequest.getRequestHeaders()).thenReturn(headers);
    when(containerRequest.getMethod()).thenReturn("GET");
    when(containerRequest.getQueryParameters()).thenReturn(queryParameters);
    when(containerRequest.getPath()).thenReturn("example/resource.html");

    String actualCanonicalRepresentation = HmacUtils.createCanonicalRepresentation(containerRequest);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-get.txt");

    assertThat(actualCanonicalRepresentation, equalTo(expectedCanonicalRepresentation));

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

    // Mock query parameters
    InBoundHeaders queryParameters = new InBoundHeaders();
    queryParameters.put("sort", Lists.newArrayList("header footer"));
    queryParameters.put("order", Lists.newArrayList("ASC"));

    // Mock request
    ContainerRequest containerRequest = mock(ContainerRequest.class);
    when(containerRequest.getRequestHeaders()).thenReturn(headers);
    when(containerRequest.getMethod()).thenReturn("POST");
    when(containerRequest.getQueryParameters()).thenReturn(queryParameters);
    when(containerRequest.getPath()).thenReturn("example/resource.html");

    // Simulate a user wrapped in HAL
    ByteArrayInputStream bais = new ByteArrayInputStream("{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}".getBytes());
    when(containerRequest.getEntityInputStream()).thenReturn(bais);

    String actualCanonicalRepresentation = HmacUtils.createCanonicalRepresentation(containerRequest);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-post.txt");

    assertThat(actualCanonicalRepresentation, equalTo(expectedCanonicalRepresentation));

  }


}
