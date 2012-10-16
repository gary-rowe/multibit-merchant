package org.multibit.mbm.client;


import com.google.common.collect.Lists;
import com.yammer.dropwizard.client.JerseyClient;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.model.PublicItem;

import java.net.URI;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicMerchantClientTest {

  private JerseyClient client=mock(JerseyClient.class);

  @Test
  public void items_retrieveAllByPage() {

    List<PublicItem> publicItems = Lists.newArrayList(
      new PublicItem(),
      new PublicItem()
    );
    URI uri = URI.create("http://localhost:8080/mbm/items?pn=0&ps=1");

    when(client.get(uri, HalMediaType.APPLICATION_JSON_TYPE, List.class)).thenReturn(publicItems);

    List<PublicItem> items = PublicMerchantClient
      .newInstance(client)
      .items()
      .retrieveAllByPage();

    assertEquals("Unexpected number of items", 2, items.size());

  }

}