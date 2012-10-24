package org.multibit.mbm.client.handlers.user;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.client.PublicMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.model.PublicItem;
import org.multibit.mbm.test.FixtureAsserts;

import java.net.URI;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CustomerUserHandlerTest extends BaseHandlerTest {

  @Test
  public void items_retrievePromotionalItemsByPage() throws Exception {

    // Arrange
    URI expectedUri = URI.create("http://localhost:8080/mbm/items/promotion?pn=0&ps=1");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    when(webResource.get(String.class)).thenReturn(
      FixtureAsserts.jsonFixture("/fixtures/hal/item/expected-public-retrieve-items-page-1.json")
    );

    // Act
    List<PublicItem> items = PublicMerchantClient
      .newInstance(locale)
      .items()
      .retrievePromotionalItemsByPage(0,1);

    // Assert
    assertEquals("Unexpected number of items", 1, items.size());

  }

  @Test
  public void item_retrieveBySku() throws Exception {

    // Arrange

    URI expectedUri = URI.create("http://localhost:8080/mbm/items/0575088893");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    when(webResource.get(String.class))
      .thenReturn(
        FixtureAsserts.fixture("/fixtures/hal/item/expected-customer-retrieve-item.json"));

    // Act
    Optional<PublicItem> optionalItem = PublicMerchantClient
      .newInstance(locale)
      .item()
      .retrieveBySku("0575088893");

    // Assert
    assertTrue(optionalItem.isPresent());

    PublicItem actualItem = optionalItem.get();

    assertEquals("0575088893",actualItem.getSKU());
    assertEquals("The Quantum Thief",actualItem.getOptionalProperties().get("title"));
  }

}
