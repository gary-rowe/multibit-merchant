package org.multibit.mbm.client.handlers.item;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.client.PublicMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.interfaces.rest.api.item.ItemDto;
import org.multibit.mbm.testing.FixtureAsserts;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ClientItemHandlerTest extends BaseHandlerTest {

  @Test
  public void retrieveBySku() throws Exception {

    // Arrange

    URI expectedUri = URI.create("http://localhost:8080/mbm/items/0575088893");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    when(webResource.get(String.class))
      .thenReturn(
        FixtureAsserts.fixture("/fixtures/hal/item/expected-customer-retrieve-item.json"));

    // Act
    Optional<ItemDto> optionalItem = PublicMerchantClient
      .newInstance(locale)
      .item()
      .retrieveBySku("0575088893");

    // Assert
    assertTrue(optionalItem.isPresent());

    ItemDto actualItem = optionalItem.get();

    assertEquals("0575088893",actualItem.getSKU());
    assertEquals("The Quantum Thief",actualItem.getOptionalProperties().get("title"));
  }

}
