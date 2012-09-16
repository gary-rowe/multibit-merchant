package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Lists;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.core.header.InBoundHeaders;
import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HmacRestrictedToInjectableTest {

  @Test
  public void testCanonicalRepresentation_AllFields_Get() throws Exception {

    HmacAuthenticator authenticator = new HmacAuthenticator();

    HmacRestrictedToInjectable<User> testObject = new HmacRestrictedToInjectable<User>(
      authenticator,
      "REST",
      new Authority[] {Authority.ROLE_ADMIN});

    // Mock headers
    InBoundHeaders headers = new InBoundHeaders();
    headers.add(HttpHeaders.DATE,"Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST,"www.example.org");
    headers.add(HttpHeaders.USER_AGENT,"curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE,"Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE,"Thohn2Mohd2zugo");

    // Mock query parameters
    InBoundHeaders queryParameters = new InBoundHeaders();
    queryParameters.put("sort", Lists.newArrayList("header footer"));
    queryParameters.put("order", Lists.newArrayList("ASC"));

    // Mock request
    HttpRequestContext requestContext = mock(HttpRequestContext.class);
    when(requestContext.getRequestHeaders()).thenReturn(headers);
    when(requestContext.getMethod()).thenReturn("GET");
    when(requestContext.getQueryParameters()).thenReturn(queryParameters);
    when(requestContext.getPath()).thenReturn("example/resource.html");

    // Mock context
    HttpContext httpContext = mock(HttpContext.class);
    when(httpContext.getRequest()).thenReturn(requestContext);

    String actualCanonicalRepresentation = testObject.createCanonicalRepresentation(httpContext);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-get.txt");

    assertThat(actualCanonicalRepresentation,equalTo(expectedCanonicalRepresentation));

  }

  @Test
  public void testCanonicalRepresentation_AllFields_Post() throws Exception {

    HmacAuthenticator authenticator = new HmacAuthenticator();

    HmacRestrictedToInjectable<User> testObject = new HmacRestrictedToInjectable<User>(
      authenticator,
      "REST",
      new Authority[] {Authority.ROLE_ADMIN});

    // Mock headers
    InBoundHeaders headers = new InBoundHeaders();
    headers.add(HttpHeaders.DATE,"Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST,"www.example.org");
    headers.add(HttpHeaders.USER_AGENT,"curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE,"Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE,"Thohn2Mohd2zugo");

    // Mock query parameters
    InBoundHeaders queryParameters = new InBoundHeaders();
    queryParameters.put("sort", Lists.newArrayList("header footer"));
    queryParameters.put("order", Lists.newArrayList("ASC"));

    // Mock request
    HttpRequestContext requestContext = mock(HttpRequestContext.class);
    when(requestContext.getRequestHeaders()).thenReturn(headers);
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getQueryParameters()).thenReturn(queryParameters);
    when(requestContext.getPath()).thenReturn("example/resource.html");
    // Simulate a user wrapped in HAL
    when(requestContext.getEntity(String.class)).thenReturn("{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}");

    // Mock context
    HttpContext httpContext = mock(HttpContext.class);
    when(httpContext.getRequest()).thenReturn(requestContext);

    String actualCanonicalRepresentation = testObject.createCanonicalRepresentation(httpContext);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-post.txt");

    assertThat(actualCanonicalRepresentation,equalTo(expectedCanonicalRepresentation));

  }

}
