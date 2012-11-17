package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.core.model.*;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.UserDao;
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

    Optional<User> user = userDao.getByApiKey("alice123");
    assertTrue("Unexpected missing user",user.isPresent());

    Customer customer = user.get().getCustomer();
    Cart expectedCart = CartBuilder
      .newInstance()
      .withCustomer(customer)
      .build();

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
    Optional<Item> book1 = itemDao.getBySKU("0099410672");
    Optional<Item> book2 = itemDao.getBySKU("0140296034");

    expectedCart.setItemQuantity(book1.get(), 1);
    expectedCart.setItemQuantity(book2.get(), 2);
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
    assertThat("Unexpected index for book1", expectedCart.getCartItemByItem(book1.get()).get().getIndex(), equalTo(0));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1.get()).get().getQuantity(), equalTo(1));
    assertThat("Unexpected index for book2", expectedCart.getCartItemByItem(book2.get()).get().getIndex(), equalTo(1));
    assertThat("Unexpected quantity for book2", expectedCart.getCartItemByItem(book2.get()).get().getQuantity(), equalTo(2));

    expectedCart.setItemQuantity(book1.get(), 4);
    expectedCart.setItemQuantity(book2.get(), 5);
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
    assertThat("Unexpected index for book1", expectedCart.getCartItemByItem(book1.get()).get().getIndex(), equalTo(0));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertThat("Unexpected index for book2", expectedCart.getCartItemByItem(book2.get()).get().getIndex(), equalTo(1));
    assertThat("Unexpected quantity for book2", expectedCart.getCartItemByItem(book2.get()).get().getQuantity(), equalTo(5));

    // Perform an update to the Cart that cascades to a delete in join table
    // due to an addition to the linked reference
    expectedCart.setItemQuantity(book2.get(), 0);
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
    assertThat("Unexpected index for book1", expectedCart.getCartItemByItem(book1.get()).get().getIndex(), equalTo(0));
    assertThat("Unexpected quantity for book1", expectedCart.getCartItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertFalse("Unexpected existence for book2", expectedCart.getCartItemByItem(book2.get()).isPresent());

  }

}
