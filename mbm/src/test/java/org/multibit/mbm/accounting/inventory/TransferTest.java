package org.multibit.mbm.accounting.inventory;

import org.joda.time.DateTime;
import org.junit.Test;
import org.multibit.mbm.accounting.Account;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TransferTest {

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureLHSNull() {

    // Configure supporting objects
    Account<InventoryEntry> account = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry d1 = new InventoryEntry(ItemType.QUANTITY,account, 10, then, then);

    new Transfer<InventoryEntry>(null, d1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureRHSNull() {

    // Configure supporting objects
    Account<InventoryEntry> account = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry w1 = new InventoryEntry(ItemType.QUANTITY,account, -10, then, then);

    new Transfer<InventoryEntry>(w1, null);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureUnbalanced() {

    // Configure supporting objects
    Account<InventoryEntry> account1 = new Account<InventoryEntry>();
    Account<InventoryEntry> account2 = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry w1 = new InventoryEntry(ItemType.QUANTITY,account1, -10, then, then);
    InventoryEntry d1 = new InventoryEntry(ItemType.QUANTITY,account2, 15, then, then);

    new Transfer<InventoryEntry>(w1, d1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureSameAccount() {

    // Configure supporting objects
    Account<InventoryEntry> account1 = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry w1 = new InventoryEntry(ItemType.QUANTITY,account1, -10, then, then);
    InventoryEntry d1 = new InventoryEntry(ItemType.QUANTITY,account1, 10, then, then);

    new Transfer<InventoryEntry>(w1, d1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureMismatchedItemType() {

    // Configure supporting objects
    Account<InventoryEntry> account1 = new Account<InventoryEntry>();
    Account<InventoryEntry> account2 = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry w1 = new InventoryEntry(ItemType.QUANTITY,account1, -10, then, then);
    InventoryEntry d1 = new InventoryEntry(ItemType.DIMENSION,account2, 10, then, then);

    new Transfer<InventoryEntry>(w1, d1);

  }

  @Test
  public void testBalanceAfterCommit() {

    // Configure supporting objects
    Account<InventoryEntry> account1 = new Account<InventoryEntry>();
    Account<InventoryEntry> account2 = new Account<InventoryEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    InventoryEntry w1 = new InventoryEntry(ItemType.QUANTITY,account1, -10, then, then);
    InventoryEntry d1 = new InventoryEntry(ItemType.QUANTITY,account2, 10, then, then);

    Transfer<InventoryEntry> tx = new Transfer<InventoryEntry>(w1, d1);
    tx.commit();

    assertThat(account1.getBalance(), equalTo(-10L));
    assertThat(account2.getBalance(), equalTo(10L));

  }

}
