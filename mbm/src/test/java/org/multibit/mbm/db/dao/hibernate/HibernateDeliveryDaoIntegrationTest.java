package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.SupplierDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Delivery;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateDeliveryDaoIntegrationTest extends BaseIntegrationTests {

  @Resource(name = "hibernateDeliveryDao")
  private DeliveryDao testObject;

  @Resource(name = "hibernateSupplierDao")
  private SupplierDao supplierDao;

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  @Test
  public void testPersist() {

    Optional<User> user = userDao.getByApiKey("steve123");
    assertTrue("Unexpected missing user",user.isPresent());

    Delivery expectedDelivery = new Delivery(user.get().getSupplier());

    // Persist with insert (new delivery)
    int originalDeliveryRows = countRowsInTable("deliveries");
    int originalItemRows = countRowsInTable("items");
    int originalSupplierRows = countRowsInTable("suppliers");
    int originalDeliveryItemRows = countRowsInTable("delivery_items");
    expectedDelivery = testObject.saveOrUpdate(expectedDelivery);

    // Session flush: Expect an insert in deliveries only
    int updatedDeliveryRows = countRowsInTable("deliveries");
    int updatedItemRows = countRowsInTable("items");
    int updatedSupplierRows = countRowsInTable("suppliers");
    int updatedDeliveryItemRows = countRowsInTable("delivery_items");
    assertThat("Expected session flush for first insert", updatedDeliveryRows, equalTo(originalDeliveryRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in delivery_items", updatedDeliveryItemRows, equalTo(originalDeliveryItemRows));

    // Perform an update to the Delivery that cascades to an insert in join table
    Optional<Item> book1 = itemDao.getBySKU("0099410672");
    Optional<Item> book2 = itemDao.getBySKU("0140296034");

    expectedDelivery.setItemQuantity(book1.get(), 1);
    expectedDelivery.setItemQuantity(book2.get(), 2);
    expectedDelivery = testObject.saveOrUpdate(expectedDelivery);
    testObject.flush();

    // Session flush: Expect no change to deliveries, 2 inserts into delivery_items
    updatedDeliveryRows = countRowsInTable("deliveries");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedDeliveryItemRows = countRowsInTable("delivery_items");
    assertThat("Unexpected data is deliveries", updatedDeliveryRows, equalTo(originalDeliveryRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in delivery_items", updatedDeliveryItemRows, equalTo(originalDeliveryItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedDelivery.getDeliveryItemByItem(book1.get()).get().getQuantity(), equalTo(1));
    assertThat("Unexpected quantity for book2", expectedDelivery.getDeliveryItemByItem(book2.get()).get().getQuantity(), equalTo(2));

    expectedDelivery.setItemQuantity(book1.get(), 4);
    expectedDelivery.setItemQuantity(book2.get(), 5);
    expectedDelivery = testObject.saveOrUpdate(expectedDelivery);
    testObject.flush();

    // Session flush: Expect no change to deliveries, delivery_items
    updatedDeliveryRows = countRowsInTable("deliveries");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedDeliveryItemRows = countRowsInTable("delivery_items");
    assertThat("Unexpected data is deliveries", updatedDeliveryRows, equalTo(originalDeliveryRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in delivery_items", updatedDeliveryItemRows, equalTo(originalDeliveryItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedDelivery.getDeliveryItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertThat("Unexpected quantity for book2", expectedDelivery.getDeliveryItemByItem(book2.get()).get().getQuantity(), equalTo(5));

    // Perform an update to the Delivery that cascades to a delete in join table
    // due to an addition to the linked reference
    expectedDelivery.setItemQuantity(book2.get(), 0);
    testObject.saveOrUpdate(expectedDelivery);
    testObject.flush();

    // Session flush: Expect no change to deliveries, items, supplier - only a delete from delivery_items
    updatedDeliveryRows = countRowsInTable("deliveries");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedDeliveryItemRows = countRowsInTable("delivery_items");
    assertThat("Unexpected data is deliveries", updatedDeliveryRows, equalTo(originalDeliveryRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in delivery_items", updatedDeliveryItemRows, equalTo(originalDeliveryItemRows + 1));
    assertThat("Unexpected quantity for book1", expectedDelivery.getDeliveryItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertFalse("Unexpected existence for book2", expectedDelivery.getDeliveryItemByItem(book2.get()).isPresent());

  }

}
