package org.multibit.mbm.client.handlers.user;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.auth.webform.WebFormClientRegistration;
import org.multibit.mbm.client.PublicMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.model.ClientUser;
import org.multibit.mbm.model.PublicItem;
import org.multibit.mbm.test.FixtureAsserts;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class PublicUserHandlerTest extends BaseHandlerTest {

  @Test
  public void user_registerWithWebForm() throws Exception {

    // Arrange
    URI expectedUri = URI.create("http://localhost:8080/mbm/client/user/register");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    when(webResource.get(String.class)).thenReturn(
      FixtureAsserts.jsonFixture("/fixtures/hal/item/expected-public-create-user.json")
    );

    WebFormClientRegistration registration = new WebFormClientRegistration(
      "charlie",
      "charlie1" // TODO Digest
    );

    // Act
    Optional<ClientUser> publicUser = PublicMerchantClient
      .newInstance(locale)
      .user()
      .registerWithWebForm(registration);

    // Assert
    assertTrue("Expected user", publicUser.isPresent());

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

    assertEquals("0575088893", actualItem.getSKU());
    assertEquals("The Quantum Thief", actualItem.getOptionalProperties().get("title"));
  }

}
