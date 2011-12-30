package org.multibit.mbm.accounting;

import org.multibit.mbm.util.ValidationUtils;

/**
 * <p>Multi-legged transaction to provide the following to {@link Account}:</p>
 * <ul>
 * <li>Provision of a collection of entries associated with a single operation</li>
 * </ul>
 * <p>A Transaction links a withdrawal from one Account to a deposit in another.</p>
 * <p>The sum of all entries in a Transaction is zero to represent the
 * conservation of the underlying items.</p>
 *
 * @since 1.0.0
 *         
 */
public class Transaction<T extends Entry<T>> {
  private final T withdrawalEntry;
  private final T depositEntry;

  private boolean isCommitted = false;

  /**
   * @param withdrawalEntry The entry to be taken from the source Account (normally -ve)
   * @param depositEntry    The entry to be deposited in the target Account (normally +ve)
   */
  public Transaction(T withdrawalEntry, T depositEntry) {
    // Apply class-specific validation rules
    validate(withdrawalEntry, depositEntry);

    // Must be OK to be here
    this.withdrawalEntry = withdrawalEntry;
    this.depositEntry = depositEntry;
  }

  protected void validate(T withdrawalEntry, T depositEntry) {
    // TODO Introduce an Exception providing SLF4J formatting syntax
    // ("Entries are not balanced. Withdrawal={}, deposit={}",withdrawalEntry.getAmount(), depositEntry.getAmount()

    // Validation
    ValidationUtils.isNotNull(withdrawalEntry, "withdrawalEntry");
    ValidationUtils.isNotNull(depositEntry, "depositEntry");

    if (withdrawalEntry.getAmount() + depositEntry.getAmount() != 0) {
      throw new IllegalArgumentException("Entries are not balanced");
    }
    if (withdrawalEntry.getAccount().equals(depositEntry.getAccount())) {
      throw new IllegalArgumentException("Entries are are against the same account");
    }
  }

  /**
   * All Transactions should enforce single commit behaviour but
   * this method not made final to allow for easier testing and
   * sub-classing.
   */
  public void commit() {
    if (!isCommitted()) {
      Account<T> withdrawalAccount = withdrawalEntry.getAccount();
      Account<T> depositAccount = depositEntry.getAccount();

      withdrawalAccount.addEntry(withdrawalEntry);
      depositAccount.addEntry(depositEntry);
    } else {
      throw new IllegalStateException("Should not commit more than once.");
    }
  }

  /**
   * @return True if this transaction has been committed
   */
  public boolean isCommitted() {
    return isCommitted;
  }

  /**
   * @return The withdrawal entry to subclasses
   */
  protected T getWithdrawalEntry() {
    return withdrawalEntry;
  }

  /**
   * @return The withdrawal entry to subclasses
   */
  protected T getDepositEntry() {
    return depositEntry;
  }

  @Override
  public String toString() {
    return String.format("Transaction[withdrawalEntry=%s, depositEntry=%s]]", withdrawalEntry.getAmount(), depositEntry.getAmount());
  }
}
