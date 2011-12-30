package org.multibit.mbm.accounting;

import org.multibit.mbm.util.ValidationUtils;

/**
 * <p>Single entry transaction to provide the following to {@link Transaction}:</p>
 * <ul>
 * <li>Ability to service memo accounts</li>
 * </ul>
 * <p>A SingleEntryTransaction is a special case of Transaction that permit a
 * single Entry and therefore unbalanced. Only certain types of Account allow
 * unbalanced Entry instances, typically a MemoAccount.</p>
 *
 * @since 1.0.0
 *         
 */
public class SingleEntryTransaction<T extends Entry<T>> extends Transaction<T> {

  public SingleEntryTransaction(T depositEntry) {
    super(null, depositEntry);
  }

  @Override
  protected void validate(T withdrawalEntry, T depositEntry) {

    // Validation
    ValidationUtils.isNull(withdrawalEntry, "withdrawalEntry");
    ValidationUtils.isNotNull(depositEntry, "depositEntry");

  }

  @Override
  public void commit() {
    if (!isCommitted()) {
      // There is no withdrawal entry
      Account<T> depositAccount = getDepositEntry().getAccount();
      depositAccount.addEntry(getDepositEntry());
    } else {
      throw new IllegalStateException("Should not commit more than once.");
    }
  }
}
