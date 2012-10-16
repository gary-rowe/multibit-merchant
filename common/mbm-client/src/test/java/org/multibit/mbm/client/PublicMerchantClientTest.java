package org.multibit.mbm.client;


import com.yammer.dropwizard.client.JerseyClient;
import org.junit.Test;
import org.multibit.mbm.client.handlers.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.handlers.item.PublicItemHandler;

import java.util.Locale;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class PublicMerchantClientTest {

  JerseyClient client = mock(JerseyClient.class);

  @Test
  public void verifyItemCollectionHandler() {

    // Arrange
    Locale locale = Locale.UK;

    // Act
    PublicItemCollectionHandler handler = PublicMerchantClient
      .newInstance(client, locale)
      .items();

    // Assert
    assertNotNull(handler);

  }

  @Test
  public void verifyItemHandler() {

    // Arrange
    Locale locale = Locale.UK;

    // Act
    PublicItemHandler handler = PublicMerchantClient
      .newInstance(client, locale)
      .item();

    // Assert
    assertNotNull(handler);

  }

}