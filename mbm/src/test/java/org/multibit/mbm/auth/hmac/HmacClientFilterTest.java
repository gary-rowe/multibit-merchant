package org.multibit.mbm.auth.hmac;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.client.impl.ClientRequestImpl;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Providers;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HmacClientFilterTest {

  @Test
  public void testCanonicalRepresentation_AllFields_Get() throws Exception {

    // TODO Implement this
    Providers providers = null;

    HmacClientFilter testObject = new HmacClientFilter("abc123","def456", providers);

    OutBoundHeaders headers = new OutBoundHeaders();
    headers.add(HttpHeaders.DATE,"Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST,"www.example.org");
    headers.add(HttpHeaders.USER_AGENT,"curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE,"Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE,"Thohn2Mohd2zugo");

    URI uri = new URI("http://example.org/example/resource.html?sort=header%20footer&order=ASC");

    ClientRequest clientRequest = new ClientRequestImpl(uri,"get",null,headers);

    String actualCanonicalRepresentation = testObject.createCanonicalRepresentation(clientRequest);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-get.txt");

    assertThat(actualCanonicalRepresentation,equalTo(expectedCanonicalRepresentation));

  }

  @Test
  public void testCanonicalRepresentation_AllFields_Post() throws Exception {

    // TODO Implement this
    Providers providers = null;

    HmacClientFilter testObject = new HmacClientFilter("abc123","def456", providers);

    OutBoundHeaders headers = new OutBoundHeaders();
    headers.add(HttpHeaders.DATE,"Sat, 01 Jan 2000 12:34:56 GMT");
    headers.add(HttpHeaders.HOST,"www.example.org");
    headers.add(HttpHeaders.USER_AGENT,"curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3");
    headers.add(HmacUtils.X_HMAC_DATE,"Sat, 01 Jan 2000 12:34:57 GMT");
    headers.add(HmacUtils.X_HMAC_NONCE,"Thohn2Mohd2zugo");

    URI uri = new URI("http://example.org/example/resource.html?sort=header%20footer&order=ASC");

    String entity = "{\"_links\":{\"self\":{\"href\":\"http://example.org/user\"}}}";

    ClientRequest clientRequest = new ClientRequestImpl(uri,"post",entity, headers);

    String actualCanonicalRepresentation = testObject.createCanonicalRepresentation(clientRequest);

    String expectedCanonicalRepresentation = FixtureHelpers.fixture("fixtures/hmac/expected-canonical-post.txt");

    assertThat(actualCanonicalRepresentation,equalTo(expectedCanonicalRepresentation));

  }


}
