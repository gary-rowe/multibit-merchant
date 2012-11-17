package org.multibit.mbm.resources.cart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.cart.AdminUpdateCartRequest;
import org.multibit.mbm.api.request.cart.PublicCartItem;
import org.multibit.mbm.core.model.Cart;
import org.multibit.mbm.core.model.Item;
import org.multibit.mbm.core.model.Role;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminCartResourceTest extends BaseJerseyHmacResourceTest {

  private final CartDao cartDao=mock(CartDao.class);
  private final ItemDao itemDao=mock(ItemDao.class);

  private final AdminCartResource testObject=new AdminCartResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User adminUser = setUpTrentHmacAuthenticator();
    adminUser.setId(1L);

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();

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

    aliceCart.setItemQuantity(book1, 1);
    aliceCart.setItemQuantity(book2, 2);

    // Configure Bob's Cart with Items
    Cart bobCart = bobUser.getCustomer().getCart();

    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    bobCart.setItemQuantity(book3, 3);
    bobCart.setItemQuantity(book4, 4);

    // Create some mock results
    List<Cart> cartList1 = Lists.newArrayList(aliceCart);
    List<Cart> cartList2 = Lists.newArrayList(bobCart);

    // Configure Cart DAO
    when(cartDao.getById(aliceCart.getId())).thenReturn(Optional.of(aliceCart));
    when(cartDao.getById(bobCart.getId())).thenReturn(Optional.of(bobCart));
    when(cartDao.getAllByPage(1, 0)).thenReturn(Lists.newLinkedList(cartList1));
    when(cartDao.getAllByPage(1, 1)).thenReturn(Lists.newLinkedList(cartList2));
    when(cartDao.saveOrUpdate(aliceCart)).thenReturn(aliceCart);
    when(cartDao.saveOrUpdate(bobCart)).thenReturn(bobCart);

    // Configure Item DAO
    when(itemDao.getBySKU(book1.getSKU())).thenReturn(Optional.of(book1));
    when(itemDao.getBySKU(book2.getSKU())).thenReturn(Optional.of(book2));
    when(itemDao.getBySKU(book3.getSKU())).thenReturn(Optional.of(book3));
    when(itemDao.getBySKU(book4.getSKU())).thenReturn(Optional.of(book4));

    testObject.setCartDao(cartDao);
    testObject.setItemDao(itemDao);

    // Configure resources
    addSingleton(testObject);

  }
  @Test
  public void adminRetrieveCartsAsHalJson() throws Exception {

    String actualResponse = configureAsClient(AdminCartResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-admin-retrieve-carts-page-1.json");

    actualResponse = configureAsClient(AdminCartResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-admin-retrieve-carts-page-2.json");

  }

  @Test
  public void adminUpdateCartAsHalJson() throws Exception {

    // Starting condition is Alice has {book1: 1, book2: 2}
    // Ending condition is Alice has {book1: 0, book2: 2, book3: 3}

    AdminUpdateCartRequest updateCartRequest = new AdminUpdateCartRequest();
    updateCartRequest.setId(1L);
    // Add a few new items
    updateCartRequest.getCartItems().add(new PublicCartItem("0316184136",3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new PublicCartItem("0099410672",0));

    String actualResponse = configureAsClient("/admin/carts/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/cart/expected-admin-update-cart.json");

  }

}
