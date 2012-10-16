package org.multibit.mbm.client;


import com.google.common.collect.Lists;
import com.sun.jersey.api.client.WebResource;
import com.yammer.dropwizard.client.JerseyClient;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.model.PublicItem;

import javax.ws.rs.core.HttpHeaders;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicMerchantClientTest {

  private JerseyClient client=mock(JerseyClient.class);
  private WebResource webResource = mock(WebResource.class);
  private WebResource.Builder builder = mock(WebResource.Builder.class);

  @Test
  public void items_retrieveAllByPage() {

    Locale locale = Locale.UK;

    List<PublicItem> publicItems = Lists.newArrayList(
      new PublicItem(),
      new PublicItem()
    );
    URI uri = URI.create("http://localhost:8080/mbm/items?pn=0&ps=1");

    when(client.resource(uri)).thenReturn(webResource);
    when(webResource.accept(HalMediaType.APPLICATION_HAL_JSON)).thenReturn(builder);
    when(builder.header(HttpHeaders.ACCEPT_LANGUAGE,"en_GB")).thenReturn(builder);
    when(builder.get(List.class)).thenReturn(publicItems);

    List<PublicItem> items = PublicMerchantClient
      .newInstance(client, locale)
      .items()
      .retrieveAllByPage();

    assertEquals("Unexpected number of items", 2, items.size());

  }

}