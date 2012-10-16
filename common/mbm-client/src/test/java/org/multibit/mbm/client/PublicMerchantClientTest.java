package org.multibit.mbm.client;


import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.model.PublicItem;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class PublicMerchantClientTest extends BaseClientTest {

  @Test
  public void items_retrievePromotionalItemsByPage() {

    // Arrange
    Locale locale = Locale.UK;

    List<PublicItem> publicItems = Lists.newArrayList(
      new PublicItem(),
      new PublicItem()
    );
    URI uri = URI.create("http://localhost:8080/mbm/items?pn=0&ps=1");

    // Test-specific JerseyClient behaviour
    when(client.resource(uri)).thenReturn(webResource);
    when(builder.get(List.class)).thenReturn(publicItems);

    // Act
    List<PublicItem> items = PublicMerchantClient
      .newInstance(client, locale)
      .items()
      .retrievePromotionalItemsByPage(0, 1);

    // Assert
    assertEquals("Unexpected number of items", 2, items.size());

  }

  @Test
  public void item_retrieveById() {

    // Arrange
    Locale locale = Locale.UK;

    PublicItem item = new PublicItem();
    URI uri = URI.create("http://localhost:8080/mbm/items/1");

    // Test-specific JerseyClient behaviour
    when(client.resource(uri)).thenReturn(webResource);
    when(builder.get(PublicItem.class)).thenReturn(item);

    // Act
    PublicItem actualItem = PublicMerchantClient
      .newInstance(client, locale)
      .item()
      .retrieveById(1);

    // Assert
    assertNotNull("Unexpected number of items", actualItem);

  }

}