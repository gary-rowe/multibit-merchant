package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.AdminUpdateCartRequest;
import org.multibit.mbm.api.request.cart.CustomerCartItem;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dto.*;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminCartResourceTest extends BaseJerseyResourceTest {

  private final CartDao cartDao=mock(CartDao.class);

  private final AdminCartResource testObject=new AdminCartResource();

  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role adminRole = DatabaseLoader.buildAdminRole();
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the admin User for authenticated access
    User adminUser = setUpAuthenticator(Lists.newArrayList(adminRole));
    adminUser.setId(1L);

    // Create some Customers
    User aliceUser = DatabaseLoader.buildAliceCustomer(customerRole);
    aliceUser.setId(1L);
    aliceUser.getCustomer().setId(1L);
    aliceUser.getCustomer().getCart().setId(1L);

    User bobUser = DatabaseLoader.buildBobCustomer(customerRole);
    bobUser.setId(2L);
    bobUser.getCustomer().setId(2L);
    bobUser.getCustomer().getCart().setId(2L);

    // Configure Alice's Cart with Items
    Cart aliceCart = aliceUser.getCustomer().getCart();

    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);

    CartItem aliceCartItem1 = new CartItem(aliceCart, book1);
    aliceCartItem1.setQuantity(1);
    CartItem aliceCartItem2 = new CartItem(aliceCart, book2);
    aliceCartItem2.setQuantity(2);

    aliceCart.getCartItems().add(aliceCartItem1);
    aliceCart.getCartItems().add(aliceCartItem2);

    // Configure Bob's Cart with Items
    Cart bobCart = bobUser.getCustomer().getCart();

    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    CartItem bobCartItem1 = new CartItem(bobCart, book3);
    bobCartItem1.setQuantity(3);
    CartItem bobCartItem2 = new CartItem(bobCart, book4);
    bobCartItem2.setQuantity(4);

    bobCart.getCartItems().add(bobCartItem1);
    bobCart.getCartItems().add(bobCartItem2);

    // Create some mock results
    List<Cart> cartList1 = Lists.newArrayList(aliceCart);
    List<Cart> cartList2 = Lists.newArrayList(bobCart);

    // Configure Cart DAO
    when(cartDao.getById(1L)).thenReturn(Optional.of(aliceCart));
    when(cartDao.getById(2L)).thenReturn(Optional.of(bobCart));
    when(cartDao.getAllByPage(1,0)).thenReturn(Lists.newLinkedList(cartList1));
    when(cartDao.getAllByPage(1,1)).thenReturn(Lists.newLinkedList(cartList2));

    testObject.setCartDao(cartDao);

    // Configure resources
    addResource(testObject);

  }
  @Test
  public void adminRetrieveCartAsHalJson() throws Exception {

    String actualResponse = client()
      .resource("/admin/cart")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 1 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/cart/expected-admin-retrieve-carts-page-1.json");

    actualResponse = client()
      .resource("/admin/cart")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 2 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/cart/expected-admin-retrieve-carts-page-2.json");

  }

  @Test
  public void adminUpdateCartAsHalJson() throws Exception {

    AdminUpdateCartRequest updateCartRequest = new AdminUpdateCartRequest();
    updateCartRequest.setId(1L);
    // Add a few new items
    updateCartRequest.getCartItems().add(new CustomerCartItem(3L,3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new CustomerCartItem(1L,0));

    String actualResponse = client()
      .resource("/admin/cart/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "fixtures/hal/cart/expected-admin-update-cart.json");

  }

}
