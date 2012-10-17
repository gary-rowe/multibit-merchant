package org.multibit.mbm.client.handlers;

import com.sun.jersey.api.client.WebResource;
import com.yammer.dropwizard.client.JerseyClient;
import org.junit.Before;
import org.junit.Ignore;
import org.multibit.mbm.api.hal.HalMediaType;

import javax.ws.rs.core.HttpHeaders;

import java.net.URI;
import java.util.Locale;

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

  protected JerseyClient client=mock(JerseyClient.class);
  protected final URI mbmBaseUri = URI.create("http://localhost:8080");
  protected final Locale locale = Locale.UK;

  protected WebResource webResource = mock(WebResource.class);
  protected WebResource.Builder builder = mock(WebResource.Builder.class);

  @Before
  public void setUpJersey() {
    when(webResource.accept(HalMediaType.APPLICATION_HAL_JSON)).thenReturn(builder);
    when(builder.header(HttpHeaders.ACCEPT_LANGUAGE,"en_GB")).thenReturn(builder);

  }

}
