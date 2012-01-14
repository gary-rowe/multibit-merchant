package org.multibit.mbm.accounting.inventory;

import org.multibit.mbm.accounting.Account;

/**
 * <p>Account to provide the following to services:</p>
 * <ul>
 * <li>Provision of an inventory-aware Account</li>
 * </ul>
 * <p>A Holding manages a set of InventoryEntry instances that
 * correspond to items.</p>
 *
 * @since 1.0.0
 *         
 */
public class Holding extends Account<InventoryEntry> {
  /**
   * The type of item that this Holding works with
   */
  private ItemType itemType = null;

  /**
   * The physical location that this Holding is for (e.g. "London warehouse")
   */
  private Location location = null;

  public Holding(ItemType itemType) {
    this.itemType = itemType;
  }

  public ItemType getItemType() {
    return itemType;
  }

  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}
