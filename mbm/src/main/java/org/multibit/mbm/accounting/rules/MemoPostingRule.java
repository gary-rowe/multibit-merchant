package org.multibit.mbm.accounting.rules;

import org.multibit.mbm.accounting.*;

/**
 * <p>Rule to provide the following to {@link org.multibit.mbm.accounting.Account}:</p>
 * <ul>
 * <li>Implementation of memo posting rule</li>
 * </ul>
 * <p>A MemoPostingRule places a copy of an entry into another designated account.
 * The source account is unaware of this copy.</p>
 *
 * @since 1.0.0
 *         
 */
public class MemoPostingRule<T extends Entry<T>> implements PostingRule<T> {
  private final Account<T> targetAccount;

  public MemoPostingRule(Account<T> targetAccount) {
    this.targetAccount = targetAccount;
  }

  @Override
  public void apply(T sourceEntry) {
    T targetEntry = sourceEntry.newInstance(targetAccount, sourceEntry.getAmount(), sourceEntry.getWhenCreated(), sourceEntry.getWhenBooked());

    // Can only get an Entry into an Account through a Transaction
    SingleEntryTransaction<T> transaction = new SingleEntryTransaction<T>(targetEntry);
    transaction.commit();
  }

  @Override
  public void reverse(T sourceEntry) {
    T targetEntry = sourceEntry.newInstance(targetAccount, -sourceEntry.getAmount(), sourceEntry.getWhenCreated(), sourceEntry.getWhenBooked());

    // Can only get an Entry into an Account through a Transaction
    SingleEntryTransaction<T> transaction = new SingleEntryTransaction<T>(targetEntry);
    transaction.commit();
  }

}
