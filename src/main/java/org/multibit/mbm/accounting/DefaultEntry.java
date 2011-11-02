package org.multibit.mbm.accounting;

import org.joda.time.DateTime;

/**
 * <p>Value object to provide the following to service layer:</p>
 * <ul>
 * <li>Provision of basic state common to all Account types</li>
 * </ul>
 * <p>An Account has zero or more Entries that represent changes to the
 * state of the Account over time. These changes of state usually represent
 * the movement of an item from one Account to another.</p>
 *
 * @since 1.0.0
 *         
 */
public class DefaultEntry implements Entry<DefaultEntry> {
  private final Account<DefaultEntry> account;
  private final long amount;
  private final DateTime whenCreated;
  private final DateTime whenBooked;

  /**
   * @param account     The Account to which this Entry will be booked
   * @param amount      The amount of the underlying (could be money, items, length)
   * @param whenCreated When this was originally created
   * @param whenBooked  When this was booked to the Account
   */
  public DefaultEntry(Account<DefaultEntry> account, long amount, DateTime whenCreated, DateTime whenBooked) {
    this.account = account;
    this.amount = amount;
    this.whenCreated = whenCreated;
    this.whenBooked = whenBooked;
  }

  /**
   * @return The Account for this Entry
   */
  public Account<DefaultEntry> getAccount() {
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

  /**
   * <p>Convenience method to provide a new instance of this Entry. It is expected that subclasses will provide their own implementations.</p>
   *
   * @param account     The Account to which this Entry will be booked
   * @param amount      The amount of the underlying (could be money, items, length)
   * @param whenCreated When this was originally created
   * @param whenBooked  When this was booked to the Account
   * @return A new partial instance of this Entry
   */
  public DefaultEntry newInstance(Account<DefaultEntry> account, long amount, DateTime whenCreated, DateTime whenBooked) {
    return new DefaultEntry(account, amount, whenCreated, whenBooked);
  }

  @Override
  public String toString() {
    return String.format("Entry[amount=%s, whenCreated='%s', whenBooked='%s']]", amount, whenCreated, whenBooked);
  }
}
