package org.multibit.mbm.accounting.inventory;

/**
 * <p>Value object to provide the following to {@link Holding}:</p>
 * <ul>
 * <li>Description of an item type</li>
 * </ul>
 * <p>An ItemType provides a unique classification against an Item</p>
 *
 * @since 1.0.0
 *         
 */
public enum ItemType {

  /**
   * An integer representation of an amount (usually a count of objects)
   */
  QUANTITY,
  /**
   * A representation of a currency (a single unit is the smallest decimal place)
   */
  CURRENCY,
  /**
   * A representation of a dimensional unit (e.g. length, mass, duration)
   */
  DIMENSION;

}
