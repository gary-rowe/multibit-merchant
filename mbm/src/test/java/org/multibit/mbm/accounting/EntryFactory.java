package org.multibit.mbm.accounting;

import org.joda.time.DateTime;
import org.multibit.mbm.accounting.inventory.InventoryEntry;
import org.multibit.mbm.accounting.inventory.ItemType;

/**
 * <p>Factory to provide the following to tests:</p>
 * <ul>
 * <li>Creation of test Entry instances</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
public enum EntryFactory {
  INSTANCE;

  /**
   *
   * @param account The Account that this Entry is for
   * @param amount  The amount associated with it
   *
   * @return A suitably configured entry with constant entry dates
   */
  public DefaultEntry buildDefaultEntry(Account<DefaultEntry> account, long amount) {

    DateTime created = new DateTime(2000, 1, 2, 12, 34, 56, 0);
    DateTime booked = new DateTime(2000, 1, 3, 0, 12, 34, 0);

    return new DefaultEntry(account, amount, created, booked);

  }

  /**
   * @param account The Account that this Entry is for
   * @param amount  The amount associated with it
   *
   * @return A suitably configured inventory entry with constant entry dates
   */
  public InventoryEntry buildSimpleInventoryEntry(Account<InventoryEntry> account, long amount) {

    DateTime created = new DateTime(2000, 1, 2, 12, 34, 56, 0);
    DateTime booked = new DateTime(2000, 1, 3, 0, 12, 34, 0);

    return new InventoryEntry(ItemType.QUANTITY, account, amount, created, booked);

  }

}
