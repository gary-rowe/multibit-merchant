package org.multibit.mbm.client.handlers.cart;

import org.junit.Test;
import org.multibit.mbm.client.CustomerMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.model.ClientCart;
import org.multibit.mbm.model.ClientUser;
import org.multibit.mbm.test.FixtureAsserts;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CustomerCartHandlerTest extends BaseHandlerTest {

  @Test
  public void cart_retrieveCart() throws Exception {

    // Arrange
    final ClientCart expectedCart = new ClientCart();
    expectedCart.setItemCount("2");

    final ClientUser clientUser = new ClientUser();
    clientUser.setApiKey("apiKey");
    clientUser.setSecretKey("secretKey");

    URI expectedUri = URI.create("http://localhost:8080/mbm/cart");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri))
      .thenReturn(webResource);
    when(webResource.get(String.class))
      .thenReturn(FixtureAsserts.jsonFixture("/fixtures/hal/cart/expected-customer-retrieve-cart.json"));

    // Act
    ClientCart actualCart = CustomerMerchantClient
      .newInstance(locale)
      .cart()
      .retrieveCart(clientUser);

    // Assert
    assertEquals("Unexpected item count", "2", actualCart.getItemCount());

  }

}
