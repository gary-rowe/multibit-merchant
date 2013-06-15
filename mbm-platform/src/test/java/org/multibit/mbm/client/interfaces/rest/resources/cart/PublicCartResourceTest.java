package org.multibit.mbm.client.interfaces.rest.resources.cart;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.client.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.client.interfaces.rest.api.cart.UpdateCartDto;
import org.multibit.mbm.client.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.client.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.client.domain.repositories.CartReadService;
import org.multibit.mbm.client.domain.repositories.ItemReadService;
import org.multibit.mbm.client.domain.model.model.Cart;
import org.multibit.mbm.client.domain.model.model.Customer;
import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.domain.model.model.User;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicCartResourceTest extends BaseJerseyHmacResourceTest {

  private final CartReadService cartDao=mock(CartReadService.class);
  private final ItemReadService itemReadService =mock(ItemReadService.class);

  private final PublicCartResource testObject=new PublicCartResource();

  @Override
  protected void setUpResources() {

    // Use Alice for Customer access
    User publicUser = setUpPublicHmacAuthenticator();
    publicUser.setId(1L);

    Customer customer = publicUser.getCustomer();
    customer.setId(1L);

    // Configure the Cart with Items
    Cart customerCart = customer.getCart();
    customerCart.setId(1L);

    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);
    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    // Include some books into the Customer Cart
    customerCart.setItemQuantity(book1,1);
    customerCart.setItemQuantity(book2,2);

    // Configure Cart DAO
    when(cartDao.saveOrUpdate(customerCart)).thenReturn(customerCart);

    // Configure Item DAO
    when(itemReadService.getBySKU(book1.getSKU())).thenReturn(Optional.of(book1));
    when(itemReadService.getBySKU(book2.getSKU())).thenReturn(Optional.of(book2));
    when(itemReadService.getBySKU(book3.getSKU())).thenReturn(Optional.of(book3));
    when(itemReadService.getBySKU(book4.getSKU())).thenReturn(Optional.of(book4));

    testObject.setCartDao(cartDao);
    testObject.setItemReadService(itemReadService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void retrieveCartAsHalJson() throws Exception {

    String actualResponse = configureAsClient(PublicCartResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Customer retrieve Cart as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-public-retrieve-cart.json");

  }

  @Test
  public void updateCartAsHalJson() throws Exception {

    // Starting condition is Customer has {book1: 1, book2: 2}
    // Ending condition is Customer has {book1: 0, book2: 2, book3: 3}

    UpdateCartDto updateCartRequest = new UpdateCartDto();
    // Add a few new items
    updateCartRequest.getCartItems().add(new PublicCartItemDto("0316184136",3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new PublicCartItemDto("0099410672",0));

    String actualResponse = configureAsClient(PublicCartResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/cart/expected-public-update-cart.json");

  }


}
