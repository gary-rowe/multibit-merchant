package org.multibit.mbm.accounting.inventory;

import org.multibit.mbm.accounting.Transaction;

/**
 * <p>Transaction to provide the following to {@link Transaction}:</p>
 * <ul>
 * <li>Additional validation of Entry in relation to inventory</li>
 * </ul>
 * <p>A Transfer is used when inventory items move from one place to
 * another. This is logged in an Account, using InventoryEntry instances
 * which are associated with a particular ItemType.</p>
 *
 * @since 1.0.0
 *         
 */
public class Transfer<T extends InventoryEntry> extends Transaction<InventoryEntry> {

  /**
   * @param withdrawalEntry The entry to be taken from the source Account (normally -ve)
   * @param depositEntry    The entry to be deposited in the target Account (normally +ve)
   */
  public Transfer(T withdrawalEntry, T depositEntry) {
    super(withdrawalEntry,depositEntry);
    validate(withdrawalEntry,depositEntry);
  }

  protected void validate(T withdrawalEntry, T depositEntry) {
    super.validate(withdrawalEntry, depositEntry);

    // Additional validation for InventoryEntry
    if (!withdrawalEntry.getItemType().equals(depositEntry.getItemType())) {
      throw new IllegalArgumentException("InventoryEntry ItemTypes must be identical");
    }

  }
}
