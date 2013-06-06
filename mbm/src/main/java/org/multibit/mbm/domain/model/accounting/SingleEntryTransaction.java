package org.multibit.mbm.domain.model.accounting;

import com.google.common.base.Preconditions;

/**
 * <p>Single entry transaction to provide the following to {@link Transaction}:</p>
 * <ul>
 * <li>Ability to service memo accounts</li>
 * </ul>
 * <p>A SingleEntryTransaction is a special case of Transaction that permit a
 * single Entry and therefore unbalanced. Only certain types of Account allow
 * unbalanced Entry instances, typically a MemoAccount.</p>
 *
 * @since 0.0.1
 *         
 */
public class SingleEntryTransaction<T extends Entry<T>> extends Transaction<T> {

  public SingleEntryTransaction(T depositEntry) {
    super(null, depositEntry);
  }

  @Override
  protected void validate(T withdrawalEntry, T depositEntry) {

    // Validation
    Preconditions.checkState(withdrawalEntry == null, "withdrawalEntry");
    Preconditions.checkNotNull(depositEntry, "depositEntry");

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
