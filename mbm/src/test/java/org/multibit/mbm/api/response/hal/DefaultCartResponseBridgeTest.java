package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.junit.Before;
import org.junit.Test;
import org.multibit.mbm.api.response.CartResponse;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartBuilder;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.test.BaseResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class DefaultCartResponseBridgeTest extends BaseResourceTest {

  @Before
  public void setUpDefaultRequestInfo() throws Exception {
    setUpUriInfo(Optional.<String>absent());
    setUpHttpHeaders(Optional.<List<MediaType>>absent());
  }

  @Test
  public void representCartResponseAsJson() throws Exception {

    Cart cart = buildCart();

    CartResponse cartResponse = new CartResponse(cart);

    DefaultCartResponseBridge testObject = new DefaultCartResponseBridge(uriInfo, principal);

    Resource resource = testObject.toResource(cartResponse);

    FixtureAsserts.assertRepresentationMatchesJsonFixture("a CartResponse can be marshalled to JSON", resource, "fixtures/hal/cart/expected-cart-by-customer.json");

  }

  @Test
  public void representCartResponseAsXml() throws IOException {

    Cart cart = buildCart();

    CartResponse cartResponse = new CartResponse(cart);

    DefaultCartResponseBridge testObject = new DefaultCartResponseBridge(uriInfo, principal);

    Resource resource = testObject.toResource(cartResponse);

    FixtureAsserts.assertRepresentationMatchesXmlFixture("a CartResponse can be marshalled to XML", resource, "fixtures/hal/cart/expected-cart-by-customer.xml");

  }

  private Cart buildCart() {
    Item item1 = DatabaseLoader.buildBookItemCryptonomicon();
    item1.setId(1L);

    Item item2 = DatabaseLoader.buildBookItemQuantumThief();
    item2.setId(2L);

    return CartBuilder
      .newInstance()
      .withId(1L)
      .withCartItem(item1, 1)
      .withCartItem(item2, 2)
      .build();
  }

}
