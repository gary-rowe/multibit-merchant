package org.multibit.mbm.client;


import com.yammer.dropwizard.client.JerseyClient;
import org.junit.Test;
import org.multibit.mbm.client.handlers.item.PublicItemCollectionHandler;
import org.multibit.mbm.client.handlers.item.PublicItemHandler;

import java.util.Locale;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class PublicMerchantClientTest {

  private final JerseyClient client = mock(JerseyClient.class);
  private final Locale locale = Locale.UK;

  @Test
  public void verifyItemCollectionHandler() {

    // Arrange

    // Act
    PublicItemCollectionHandler handler = PublicMerchantClient
      .newInstance(locale)
      .items();

    // Assert
    assertNotNull(handler);

  }

  @Test
  public void verifyItemHandler() {

    // Arrange

    // Act
    PublicItemHandler handler = PublicMerchantClient
      .newInstance(locale)
      .item();

    // Assert
    assertNotNull(handler);

  }

}