package org.multibit.mbm.accounting.inventory;

import org.junit.Test;
import org.multibit.mbm.accounting.EntryFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HoldingTest {

  @Test
  public void testInitialBalance() {
    Holding testObject = new Holding(ItemType.QUANTITY);

    assertThat(testObject.getBalance(), equalTo(0L));

  }

  @Test
  public void testBalanceAfterAdd() {

    Holding sourceHolding = new Holding(ItemType.QUANTITY);
    Holding testObject = new Holding(ItemType.QUANTITY);

    final InventoryEntry e1w = EntryFactory.INSTANCE.buildSimpleInventoryEntry(sourceHolding, 0L);
    final InventoryEntry e1d = EntryFactory.INSTANCE.buildSimpleInventoryEntry(testObject, 0L);

    // Can only get an Entry into an Account through a Transaction
    final Transfer<InventoryEntry> t1 = new Transfer<InventoryEntry>(e1w, e1d);
    t1.commit();

    assertThat(testObject.getBalance(), equalTo(0L));

    final InventoryEntry e2w = EntryFactory.INSTANCE.buildSimpleInventoryEntry(sourceHolding, -1L);
    final InventoryEntry e2d = EntryFactory.INSTANCE.buildSimpleInventoryEntry(testObject, 1L);

    // Can only get an Entry into an Account through a Transaction
    Transfer<InventoryEntry> t2 = new Transfer<InventoryEntry>(e2w, e2d);
    t2.commit();

    assertThat(testObject.getBalance(), equalTo(1L));

    final InventoryEntry e3w = EntryFactory.INSTANCE.buildSimpleInventoryEntry(sourceHolding, -2L);
    final InventoryEntry e3d = EntryFactory.INSTANCE.buildSimpleInventoryEntry(testObject, 2L);

    // Can only get an Entry into an Account through a Transaction
    final Transfer<InventoryEntry> t3 = new Transfer<InventoryEntry>(e3w, e3d);
    t3.commit();

    assertThat(testObject.getBalance(), equalTo(3L));

    final InventoryEntry e4w = EntryFactory.INSTANCE.buildSimpleInventoryEntry(sourceHolding, -3L);
    final InventoryEntry e4d = EntryFactory.INSTANCE.buildSimpleInventoryEntry(testObject, 3L);

    // Can only get an Entry into an Account through a Transaction
    Transfer<InventoryEntry> t4 = new Transfer<InventoryEntry>(e4w, e4d);
    t4.commit();

    assertThat(testObject.getBalance(), equalTo(6L));

  }

}
