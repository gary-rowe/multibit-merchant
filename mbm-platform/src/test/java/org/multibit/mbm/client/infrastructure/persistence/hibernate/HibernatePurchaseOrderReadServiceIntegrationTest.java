package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.client.domain.model.model.*;
import org.multibit.mbm.client.domain.repositories.PurchaseOrderReadService;
import org.multibit.mbm.client.domain.repositories.ItemReadService;
import org.multibit.mbm.client.domain.repositories.SupplierReadService;
import org.multibit.mbm.client.domain.repositories.UserReadService;
import org.multibit.mbm.testing.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernatePurchaseOrderReadServiceIntegrationTest extends BaseIntegrationTests {

  @Resource(name = "hibernatePurchaseOrderDao")
  private PurchaseOrderReadService testObject;

  @Resource(name = "hibernateSupplierDao")
  private SupplierReadService supplierReadService;

  @Resource(name = "hibernateItemDao")
  private ItemReadService itemReadService;

  @Resource(name = "hibernateUserDao")
  private UserReadService userReadService;

  @Test
  public void testPersist() {

    Optional<User> user = userReadService.getByApiKey("steve123");
    assertTrue("Unexpected missing user",user.isPresent());

    Supplier supplier = user.get().getSupplier();

    PurchaseOrder expectedPurchaseOrder = PurchaseOrderBuilder
      .newInstance()
      .withSupplier(supplier)
      .build();

    // Persist with insert (new purchase order)
    int originalPurchaseOrderRows = countRowsInTable("purchase_orders");
    int originalItemRows = countRowsInTable("items");
    int originalSupplierRows = countRowsInTable("suppliers");
    int originalPurchaseOrderItemRows = countRowsInTable("purchase_order_items");
    expectedPurchaseOrder = testObject.saveOrUpdate(expectedPurchaseOrder);

    // Session flush: Expect an insert in purchase orders only
    int updatedPurchaseOrderRows = countRowsInTable("purchase_orders");
    int updatedItemRows = countRowsInTable("items");
    int updatedSupplierRows = countRowsInTable("suppliers");
    int updatedPurchaseOrderItemRows = countRowsInTable("purchase_order_items");
    assertThat("Expected session flush for first insert", updatedPurchaseOrderRows, equalTo(originalPurchaseOrderRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in purchase_order_items", updatedPurchaseOrderItemRows, equalTo(originalPurchaseOrderItemRows));

    // Perform an update to the PurchaseOrder that cascades to an insert in join table
    Optional<Item> book1 = itemReadService.getBySKU("0099410672");
    Optional<Item> book2 = itemReadService.getBySKU("0140296034");

    expectedPurchaseOrder.setItemQuantity(book1.get(), 1);
    expectedPurchaseOrder.setItemQuantity(book2.get(), 2);
    expectedPurchaseOrder = testObject.saveOrUpdate(expectedPurchaseOrder);
    testObject.flush();

    // Session flush: Expect no change to purchaseOrders, 2 inserts into purchaseOrder_items
    updatedPurchaseOrderRows = countRowsInTable("purchase_orders");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedPurchaseOrderItemRows = countRowsInTable("purchase_order_items");
    assertThat("Unexpected data is purchaseOrders", updatedPurchaseOrderRows, equalTo(originalPurchaseOrderRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in purchaseOrder_items", updatedPurchaseOrderItemRows, equalTo(originalPurchaseOrderItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedPurchaseOrder.getPurchaseOrderItemByItem(book1.get()).get().getQuantity(), equalTo(1));
    assertThat("Unexpected quantity for book2", expectedPurchaseOrder.getPurchaseOrderItemByItem(book2.get()).get().getQuantity(), equalTo(2));

    expectedPurchaseOrder.setItemQuantity(book1.get(), 4);
    expectedPurchaseOrder.setItemQuantity(book2.get(), 5);
    expectedPurchaseOrder = testObject.saveOrUpdate(expectedPurchaseOrder);
    testObject.flush();

    // Session flush: Expect no change to purchaseOrders, purchaseOrder_items
    updatedPurchaseOrderRows = countRowsInTable("purchase_orders");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedPurchaseOrderItemRows = countRowsInTable("purchase_order_items");
    assertThat("Unexpected data is purchaseOrders", updatedPurchaseOrderRows, equalTo(originalPurchaseOrderRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in purchaseOrder_items", updatedPurchaseOrderItemRows, equalTo(originalPurchaseOrderItemRows + 2));
    assertThat("Unexpected quantity for book1", expectedPurchaseOrder.getPurchaseOrderItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertThat("Unexpected quantity for book2", expectedPurchaseOrder.getPurchaseOrderItemByItem(book2.get()).get().getQuantity(), equalTo(5));

    // Perform an update to the PurchaseOrder that cascades to a delete in join table
    // due to an addition to the linked reference
    expectedPurchaseOrder.setItemQuantity(book2.get(), 0);
    testObject.saveOrUpdate(expectedPurchaseOrder);
    testObject.flush();

    // Session flush: Expect no change to purchaseOrders, items, supplier - only a delete from purchaseOrder_items
    updatedPurchaseOrderRows = countRowsInTable("purchase_orders");
    updatedItemRows = countRowsInTable("items");
    updatedSupplierRows = countRowsInTable("suppliers");
    updatedPurchaseOrderItemRows = countRowsInTable("purchase_order_items");
    assertThat("Unexpected data is purchaseOrders", updatedPurchaseOrderRows, equalTo(originalPurchaseOrderRows + 1));
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows));
    assertThat("Unexpected data in suppliers", updatedSupplierRows, equalTo(originalSupplierRows));
    assertThat("Unexpected data in purchaseOrder_items", updatedPurchaseOrderItemRows, equalTo(originalPurchaseOrderItemRows + 1));
    assertThat("Unexpected quantity for book1", expectedPurchaseOrder.getPurchaseOrderItemByItem(book1.get()).get().getQuantity(), equalTo(4));
    assertFalse("Unexpected existence for book2", expectedPurchaseOrder.getPurchaseOrderItemByItem(book2.get()).isPresent());

  }

}
