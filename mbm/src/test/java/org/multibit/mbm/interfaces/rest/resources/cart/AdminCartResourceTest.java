package org.multibit.mbm.interfaces.rest.resources.cart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.common.pagination.PaginatedLists;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.domain.repositories.CartReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.interfaces.rest.api.cart.AdminUpdateCartDto;
import org.multibit.mbm.interfaces.rest.api.cart.PublicCartItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.links.cart.CartLinks;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminCartResourceTest extends BaseJerseyHmacResourceTest {

  private final CartReadService cartDao=mock(CartReadService.class);
  private final ItemReadService itemReadService =mock(ItemReadService.class);

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

    PaginatedList<Cart> page1 = PaginatedLists.newPaginatedArrayList(1, 2, cartList1);
    PaginatedList<Cart> page2 = PaginatedLists.newPaginatedArrayList(2, 2, cartList2);

    // Configure Cart DAO

    when(cartDao.getById(aliceCart.getId())).thenReturn(Optional.of(aliceCart));
    when(cartDao.getById(bobCart.getId())).thenReturn(Optional.of(bobCart));
    when(cartDao.getPaginatedList(1, 1)).thenReturn(page1);
    when(cartDao.getPaginatedList(1, 2)).thenReturn(page2);
    when(cartDao.saveOrUpdate(aliceCart)).thenReturn(aliceCart);
    when(cartDao.saveOrUpdate(bobCart)).thenReturn(bobCart);

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
  public void adminRetrieveCartsAsHalJson() throws Exception {

    String actualResponse = configureAsClient(AdminCartResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-admin-retrieve-carts-page-1.json");

    actualResponse = configureAsClient(AdminCartResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "2")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Cart list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/cart/expected-admin-retrieve-carts-page-2.json");

  }

  @Test
  public void adminUpdateCartAsHalJson() throws Exception {

    // Starting condition is Alice has {book1: 1, book2: 2}
    // Ending condition is Alice has {book1: 0, book2: 2, book3: 3}

    AdminUpdateCartDto updateCartRequest = new AdminUpdateCartDto();
    // Add a few new items
    updateCartRequest.getCartItems().add(new PublicCartItemDto("0316184136",3));
    // Remove by setting to zero
    updateCartRequest.getCartItems().add(new PublicCartItemDto("0099410672",0));

    URI uri = UriBuilder.fromPath(CartLinks.ADMIN_SELF_TEMPLATE).build(1);

    String actualResponse = configureAsClient(uri)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateCartRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateCart by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/cart/expected-admin-update-cart.json");

  }

}
