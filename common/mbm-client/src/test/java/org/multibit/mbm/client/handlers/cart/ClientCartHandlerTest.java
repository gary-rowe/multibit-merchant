package org.multibit.mbm.client.handlers.cart;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.request.cart.PublicCartItem;
import org.multibit.mbm.client.PublicMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.model.ClientCart;
import org.multibit.mbm.model.ClientUser;
import org.multibit.mbm.test.FixtureAsserts;

import java.net.URI;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ClientCartHandlerTest extends BaseHandlerTest {

  @Test
  public void retrieveCartNoItems() throws Exception {

    // Arrange
    final ClientCart expectedCart = new ClientCart();
    expectedCart.setItemTotal("2");

    final ClientUser clientUser = new ClientUser();
    clientUser.setApiKey("apiKey");
    clientUser.setSecretKey("secretKey");

    URI expectedUri = URI.create("http://localhost:8080/mbm/cart");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri))
      .thenReturn(webResource);
    when(webResource.get(String.class))
      .thenReturn(FixtureAsserts.jsonFixture("/fixtures/hal/cart/expected-public-retrieve-cart.json"));

    // Act
    ClientCart actualCart = PublicMerchantClient
      .newInstance(locale)
      .cart()
      .retrieveCartNoItems(clientUser);

    // Assert
    assertEquals("Unexpected item count", "2", actualCart.getItemTotal());

  }

  @Test
  public void retrieveCart() throws Exception {

    // Arrange
    final ClientCart expectedCart = new ClientCart();
    expectedCart.setItemTotal("2");

    final ClientUser clientUser = new ClientUser();
    clientUser.setApiKey("apiKey");
    clientUser.setSecretKey("secretKey");

    URI expectedUri = URI.create("http://localhost:8080/mbm/cart");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri))
      .thenReturn(webResource);
    when(webResource.get(String.class))
      .thenReturn(FixtureAsserts.jsonFixture("/fixtures/hal/cart/expected-public-retrieve-cart.json"));

    // Act
    ClientCart actualCart = PublicMerchantClient
      .newInstance(locale)
      .cart()
      .retrieveCart(clientUser);

    // Assert
    assertEquals("Unexpected item count", "2", actualCart.getItemTotal());

  }

  @Test
  public void updateCartItems() throws Exception {

    // Arrange
    final ClientCart expectedCart = new ClientCart();
    expectedCart.setItemTotal("2");

    final ClientUser clientUser = new ClientUser();
    clientUser.setApiKey("apiKey");
    clientUser.setSecretKey("secretKey");

    URI expectedUri = URI.create("http://localhost:8080/mbm/cart");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri))
      .thenReturn(webResource);
    // Use builder since it is a PUT
    when(builder.put(String.class))
      .thenReturn(FixtureAsserts.jsonFixture("/fixtures/hal/cart/expected-public-update-cart.json"));

    // Act
    List<PublicCartItem> cartItems = Lists.newArrayList();

    ClientCart actualCart = PublicMerchantClient
      .newInstance(locale)
      .cart()
      .updateCartItems(clientUser, cartItems);

    // Assert
    assertEquals("Unexpected item count", "2", actualCart.getItemTotal());

  }

}
