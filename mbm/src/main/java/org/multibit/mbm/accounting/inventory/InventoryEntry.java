package org.multibit.mbm.accounting.inventory;

import org.joda.time.DateTime;
import org.multibit.mbm.accounting.Account;
import org.multibit.mbm.accounting.Entry;

/**
 * <p>Value object to provide the following to {@link Holding}:</p>
 * <ul>
 * <li>Provision of basic state specific to inventory Account types (e.g. Holding)</li>
 * </ul>
 * <p>An InventoryEntry would typically represent a physical or digital item that
 * is being moved from one account to another. </p>
 *
 * @since 1.0.0
 *         
 */
public class InventoryEntry implements Entry<InventoryEntry> {
  private final ItemType itemType;

  private final Account<InventoryEntry> account;
  private final long amount;
  private final DateTime whenCreated;
  private final DateTime whenBooked;

  /**
   * <p>Convenience method to provide a new instance of this Entry. It is expected that subclasses will provide their own implementations.</p>
   *
   * @param itemType    The underlying item type (e.g. physical, digital etc)
   * @param account     The Account to which this Entry will be booked
   * @param amount      The amount of the underlying (could be money, items, length)
   * @param whenCreated When this was originally created
   * @param whenBooked  When this was booked to the Account
   */
  public InventoryEntry(ItemType itemType,
                        Account<InventoryEntry> account,
                        long amount,
                        DateTime whenCreated,
                        DateTime whenBooked) {
    validate(itemType,account,amount, whenCreated,whenBooked);
    this.itemType = itemType;
    this.account = account;
    this.amount = amount;
    this.whenCreated = whenCreated;
    this.whenBooked = whenBooked;
  }

  private void validate(ItemType itemType, Account<InventoryEntry> account, long amount, DateTime whenCreated, DateTime whenBooked) {
    // Validation
    if (itemType == null
      || account == null
      || whenCreated == null
      || whenBooked == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }

  }

  @Override
  public InventoryEntry newInstance(Account<InventoryEntry> account, long amount, DateTime whenCreated, DateTime whenBooked) {
    return new InventoryEntry(this.itemType,account, amount, whenCreated, whenBooked);
  }

  /**
   * @return The Account for this Entry
   */
  public Account<InventoryEntry> getAccount() {
    return account;
  }

  /**
   * @return The amount of units associated with the entry
   */
  public long getAmount() {
    return amount;
  }

  /**
   * @return The instant when this entry was created
   */
  public DateTime getWhenCreated() {
    return whenCreated;
  }

  /**
   * @return The instant when this entry was added to it's Account
   */
  public DateTime getWhenBooked() {
    return whenBooked;
  }


  @Override
  public String toString() {
    return String.format("Entry[amount=%s, whenCreated='%s', whenBooked='%s']]", amount, whenCreated, whenBooked);
  }

  public ItemType getItemType() {
    return itemType;
  }
}
