package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.CustomerCartItem;
import org.multibit.mbm.api.request.cart.CustomerUpdateCartRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.*;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerCartResourceTest extends BaseJerseyResourceTest {

  private final CartDao cartDao=mock(CartDao.class);
  private final ItemDao itemDao=mock(ItemDao.class);

  private final CustomerCartResource testObject=new CustomerCartResource();

  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the Customer User for authenticated access
    User customerUser = setUpAuthenticator(Lists.newArrayList(customerRole));
    customerUser.setId(1L);

    Customer customer = customerUser.getCustomer();
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
    CartItem cartItem1 = new CartItem(customerCart, book1);
    cartItem1.setQuantity(1);
    CartItem cartItem2 = new CartItem(customerCart, book2);
    cartItem2.setQuantity(2);

    customerCart.getCartItems().add(cartItem1);
    customerCart.getCartItems().add(cartItem2);

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

    CustomerUpdateCartRequest updateCartRequest = new CustomerUpdateCartRequest();
    // Add a few new items
    updateCartRequest.getCartItems().add(new CustomerCartItem(3L,3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new CustomerCartItem(1L,0));

    String actualResponse = configureAsClient("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/cart/expected-customer-update-cart.json");

  }


}
