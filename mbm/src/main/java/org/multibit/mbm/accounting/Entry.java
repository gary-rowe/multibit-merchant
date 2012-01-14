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
public interface Entry<T extends Entry<T>> {

  /**
   * @return The Account for this Entry
   */
  public Account<T> getAccount();

  /**
   * @return The amount of units associated with the entry
   */
  public long getAmount();

  /**
   * @return The instant when this entry was created
   */
  public DateTime getWhenCreated();

  /**
   * @return The instant when this entry was added to it's Account
   */
  public DateTime getWhenBooked();

  /**
   * <p>Provides a new generic instance of this Entry. It is expected that subclasses will provide their own implementations containing their mandatory fields.</p>
   *
   * @param account     The Account to which this Entry will be booked
   * @param amount      The amount of the underlying (could be money, items, length)
   * @param whenCreated When this was originally created
   * @param whenBooked  When this was booked to the Account
   * @return A new partial instance of this Entry
   */
  public T newInstance(Account<T> account, long amount, DateTime whenCreated, DateTime whenBooked);

}
