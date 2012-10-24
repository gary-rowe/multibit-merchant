package org.multibit.mbm.resources.cart;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.PublicCartItem;
import org.multibit.mbm.api.request.cart.PublicUpdateCartRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicCartResourceTest extends BaseJerseyHmacResourceTest {

  private final CartDao cartDao=mock(CartDao.class);
  private final ItemDao itemDao=mock(ItemDao.class);

  private final PublicCartResource testObject=new PublicCartResource();

  @Override
  protected void setUpResources() {

    // Use Alice for Customer access
    User publicUser = setUpAliceHmacAuthenticator();
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
    when(itemDao.getById(1L)).thenReturn(Optional.of(book1));
    when(itemDao.getById(2L)).thenReturn(Optional.of(book2));
    when(itemDao.getById(3L)).thenReturn(Optional.of(book3));
    when(itemDao.getById(4L)).thenReturn(Optional.of(book4));

    testObject.setCartDao(cartDao);
    testObject.setItemDao(itemDao);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void customerRetrieveCartAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Customer retrieve Cart as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-customer-retrieve-cart.json");

  }

  @Test
  public void customerUpdateCartAsHalJson() throws Exception {

    // Starting condition is Customer has {book1: 1, book2: 2}
    // Ending condition is Customer has {book1: 0, book2: 2, book3: 3}

    PublicUpdateCartRequest updateCartRequest = new PublicUpdateCartRequest();
    // Add a few new items
    updateCartRequest.getCartItems().add(new PublicCartItem(3L,3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new PublicCartItem(1L,0));

    String actualResponse = configureAsClient("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/cart/expected-customer-update-cart.json");

  }


}
