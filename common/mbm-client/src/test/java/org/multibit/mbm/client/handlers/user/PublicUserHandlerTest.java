package org.multibit.mbm.client.handlers.user;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.auth.webform.WebFormClientRegistration;
import org.multibit.mbm.client.PublicMerchantClient;
import org.multibit.mbm.client.handlers.BaseHandlerTest;
import org.multibit.mbm.model.ClientUser;
import org.multibit.mbm.test.FixtureAsserts;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class PublicUserHandlerTest extends BaseHandlerTest {

  @Test
  public void registerWithWebForm() throws Exception {

    // Arrange
    URI expectedUri = URI.create("http://localhost:8080/mbm/client/user/register");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    when(builder.post(String.class)).thenReturn(
      FixtureAsserts.jsonFixture("/fixtures/hal/user/expected-client-register-customer-user.json")
    );

    WebFormClientRegistration registration = new WebFormClientRegistration(
      "bob",
      "bob1"
    );

    // Act
    Optional<ClientUser> publicUser = PublicMerchantClient
      .newInstance(locale)
      .user()
      .registerWithWebForm(registration);

    // Assert
    assertTrue("Expected user", publicUser.isPresent());
    assertEquals("bob123",publicUser.get().getApiKey());
    assertEquals("bob456",publicUser.get().getSecretKey());

  }

  @Test
  public void registerAnonymously() throws Exception {

    // Arrange
    URI expectedUri = URI.create("http://localhost:8080/mbm/client/user/anonymous");

    // Test-specific JerseyClient behaviour
    when(client.resource(expectedUri)).thenReturn(webResource);
    // No entity so use resource not builder
    when(webResource.post(String.class)).thenReturn(
      FixtureAsserts.jsonFixture("/fixtures/hal/user/expected-client-register-anonymous-user.json")
    );

    // Act
    Optional<ClientUser> publicUser = PublicMerchantClient
      .newInstance(locale)
      .user()
      .registerAnonymously();

    // Assert
    assertTrue("Expected user", publicUser.isPresent());
    assertEquals("bob123",publicUser.get().getApiKey());
    assertEquals("bob456",publicUser.get().getSecretKey());

  }

}
