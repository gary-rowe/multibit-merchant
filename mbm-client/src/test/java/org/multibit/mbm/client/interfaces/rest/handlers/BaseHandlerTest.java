package org.multibit.mbm.client.interfaces.rest.handlers;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Ignore;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.HalHmacResourceFactory;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Abstract base class to provide the following to client tests:</p>
 * <ul>
 * <li>Standard mock handling</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Ignore
public abstract class BaseHandlerTest {

  protected Client client=mock(Client.class);
  protected final Locale locale = Locale.UK;

  protected WebResource webResource = mock(WebResource.class);
  protected WebResource.Builder builder = mock(WebResource.Builder.class);

  @Before
  public void setUpMocks() {

    // Configure standard mock behaviour from the Jersey client

    when(webResource.accept(HalMediaType.APPLICATION_HAL_JSON)).thenReturn(builder);
    when(webResource.header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString())).thenReturn(builder);
    when(webResource.entity(anyObject(), any(MediaType.class))).thenReturn(builder);
    when(builder.header(HttpHeaders.ACCEPT_LANGUAGE,locale.toString())).thenReturn(builder);

    // Configure the Merchant client factory
    HalHmacResourceFactory.INSTANCE.setBaseUri(URI.create("http://localhost:8080/mbm"));
    HalHmacResourceFactory.INSTANCE.setJerseyClient(client);
    HalHmacResourceFactory.INSTANCE.setClientApiKey("trent123");
    HalHmacResourceFactory.INSTANCE.setClientSecretKey("trent456");
  }

}
