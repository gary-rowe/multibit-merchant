package org.multibit.mbm.cart.dao;

import org.junit.Test;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.security.dao.UserDao;
import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.test.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateCartDaoIntegrationTest extends BaseIntegrationTests {

  @Resource(name = "hibernateCartDao")
  private CartDao testObject;

  @Resource(name = "hibernateCustomerDao")
  private CustomerDao customerDao;

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  @Test
  public void testPersist() {

    User user = userDao.getUserByUUID("alice123");
    assertNotNull("Unexpected missing user",user);

    Cart expectedCart = new Cart(user.getCustomer());

    // Persist with insert (new cart)
    int originalCartRows = countRowsInTable("carts");
    int originalItemRows = countRowsInTable("items");
    int originalCustomerRows = countRowsInTable("customers");
    int originalCartItemRows = countRowsInTable("cart_items");
    expectedCart = testObject.saveOrUpdate(expectedCart);

    // Session flush: Expect an insert in carts only
    int updatedCartRows = countRowsInTable("carts");
    int updatedItemRows = countRowsInTable("items");
    int updatedCustomerRows = countRowsInTable("customers");
    int updatedCartItemRows = countRowsInTable("cart_items");
    assertThat("Expected session flush for first insert", updatedCartRows, equalTo(originalCartRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows));
    assertThat("Unexpected data in cart_items", updatedCartItemRows, equalTo(originalCartItemRows));

    // Perform an update to the Cart that cascades to an insert in join table
    Item book1 = itemDao.getBySKU("0099410672");
    Item book2 = itemDao.getBySKU("0140296034");

    expectedCart.setItemQuantity(book1, 1);
    expectedCart.setItemQuantity(book2, 2);
    expectedCart = testObject.saveOrUpdate(expectedCart);
    testObject.flush();

    // Session flush: Expect no change to carts, 2 inserts into cart_items
    updatedCartRows = countRowsInTable("carts");
    updatedItemRows = countRowsInTable("items");
    updatedCustomerRows = countRowsInTable("customers");
    updatedCartItemRows = countRowsInTable("cart_items");
    assertThat("Unexpected data is carts", updatedCartRows, equalTo(originalCartRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows));
    assertThat("Unexpected data in cart_items", updatedCartItemRows, equalTo(originalCartItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1).getQuantity(), equalTo(1));
    assertThat("Unexpected quantity for book2", expectedCart.getCartItemByItem(book2).getQuantity(), equalTo(2));

    expectedCart.setItemQuantity(book1, 4);
    expectedCart.setItemQuantity(book2, 5);
    expectedCart = testObject.saveOrUpdate(expectedCart);
    testObject.flush();

    // Session flush: Expect no change to carts, cart_items
    updatedCartRows = countRowsInTable("carts");
    updatedItemRows = countRowsInTable("items");
    updatedCustomerRows = countRowsInTable("customers");
    updatedCartItemRows = countRowsInTable("cart_items");
    assertThat("Unexpected data is carts", updatedCartRows, equalTo(originalCartRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows));
    assertThat("Unexpected data in cart_items", updatedCartItemRows, equalTo(originalCartItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1).getQuantity(), equalTo(4));
    assertThat("Unexpected quantity for book2", expectedCart.getCartItemByItem(book2).getQuantity(), equalTo(5));

    // Perform an update to the Cart that cascades to a delete in join table
    // due to an addition to the linked reference
    expectedCart.setItemQuantity(book2, 0);
    testObject.saveOrUpdate(expectedCart);
    testObject.flush();

    // Session flush: Expect no change to carts, items, customer - only a delete from cart_items
    updatedCartRows = countRowsInTable("carts");
    updatedItemRows = countRowsInTable("items");
    updatedCustomerRows = countRowsInTable("customers");
    updatedCartItemRows = countRowsInTable("cart_items");
    assertThat("Unexpected data is carts", updatedCartRows, equalTo(originalCartRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in customers", updatedCustomerRows, equalTo(originalCustomerRows));
    assertThat("Unexpected data in cart_items", updatedCartItemRows, equalTo(originalCartItemRows + 1));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1).getQuantity(), equalTo(4));
    assertNull("Unexpected existence for book2", expectedCart.getCartItemByItem(book2));

  }

  @Test
  public void testGetInitialisedCartByCustomer() {

    User user = userDao.getUserByUUID("alice123");

    Customer customer = user.getCustomer();

    customer = customerDao.saveOrUpdate(customer);

    Cart actualCart = testObject.getInitialisedCartByCustomer(customer);

    assertNotNull("Unexpected missing Cart", actualCart);
    assertThat("Unexpected quantity for cart", actualCart.getCartItems().size(), equalTo(0));


  }

}
