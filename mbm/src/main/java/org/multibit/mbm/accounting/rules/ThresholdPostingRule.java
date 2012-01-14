package org.multibit.mbm.accounting.rules;

import org.multibit.mbm.accounting.Account;
import org.multibit.mbm.accounting.Entry;
import org.multibit.mbm.accounting.PostingRule;
import org.multibit.mbm.accounting.SingleEntryTransaction;

/**
 * <p>Rule to provide the following to {@link org.multibit.mbm.accounting.Account}:</p>
 * <ul>
 * <li>Implementation of a threshold posting rule</li>
 * </ul>
 * <p>A ThresholdPostingRule places a copy of an entry into another designated account
 * if a certain threshold value is met. The source account is unaware of this copy.</p>
 *
 * @since 1.0.0
 *         
 */
public class ThresholdPostingRule<T extends Entry<T>> implements PostingRule<T> {
  private final Account<T> targetAccount;

  private final long threshold;


  public ThresholdPostingRule(Account<T> targetAccount, long threshold) {
    this.targetAccount = targetAccount;
    this.threshold = threshold;
  }

  @Override
  public void apply(T sourceEntry) {
    if (sourceEntry.getAmount() >= threshold) {
      T targetEntry = sourceEntry.newInstance(targetAccount, sourceEntry.getAmount(), sourceEntry.getWhenCreated(), sourceEntry.getWhenBooked());

      // Can only get an Entry into an Account through a Transaction
      SingleEntryTransaction<T> transaction = new SingleEntryTransaction<T>(targetEntry);
      transaction.commit();
    }
  }

  @Override
  public void reverse(T sourceEntry) {
    T targetEntry = sourceEntry.newInstance(targetAccount, -sourceEntry.getAmount(), sourceEntry.getWhenCreated(), sourceEntry.getWhenBooked());

    // Can only get an Entry into an Account through a Transaction
    SingleEntryTransaction<T> transaction = new SingleEntryTransaction<T>(targetEntry);
    transaction.commit();
  }

}
