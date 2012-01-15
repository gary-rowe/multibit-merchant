package org.multibit.mbm.accounting.rules;

import org.junit.Test;
import org.multibit.mbm.accounting.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MemoPostingRuleTest {

  private MemoPostingRule<DefaultEntry> testObject = null;

  @Test
  public void testApply() {

    // Configure supporting objects
    Account<DefaultEntry> sourceAccount = new Account<DefaultEntry>();
    Account<DefaultEntry> targetAccount = new Account<DefaultEntry>();
    // TODO Make this a real MemoAccount
    Account<DefaultEntry> memoAccount = new Account<DefaultEntry>();

    // Use the target account for triggering the memo
    testObject = new MemoPostingRule<DefaultEntry>(memoAccount);
    targetAccount.addPostingRule(testObject);

    DefaultEntry sourceEntry = EntryFactory.INSTANCE.buildDefaultEntry(sourceAccount, -10L);
    DefaultEntry targetEntry = EntryFactory.INSTANCE.buildDefaultEntry(targetAccount, 10L);

    // Perform the test (Account will notify PostingRule)
    // Can only get an Entry into an Account through a Transaction
    Transaction<DefaultEntry> transaction = new Transaction<DefaultEntry>(sourceEntry, targetEntry);
    transaction.commit();

    // Verify the outcome
    assertThat(memoAccount.getBalance(), equalTo(10L));

  }

  @Test
  public void testReverse() {

    // Configure supporting objects
    Account<DefaultEntry> sourceAccount = new Account<DefaultEntry>();
    Account<DefaultEntry> targetAccount = new Account<DefaultEntry>();
    // TODO Make this a real MemoAccount
    Account<DefaultEntry> memoAccount = new Account<DefaultEntry>();

    // Use the target account for triggering the memo
    testObject = new MemoPostingRule<DefaultEntry>(memoAccount);
    targetAccount.addPostingRule(testObject);

    DefaultEntry sourceEntry = EntryFactory.INSTANCE.buildDefaultEntry(sourceAccount, -10L);
    DefaultEntry targetEntry = EntryFactory.INSTANCE.buildDefaultEntry(targetAccount, 10L);

    // Perform the test (Account will notify PostingRule)
    // Can only get an Entry into an Account through a Transaction
    Transaction<DefaultEntry> transaction = new Transaction<DefaultEntry>(sourceEntry, targetEntry);
    transaction.commit();

    // Perform the test
    testObject.reverse(targetEntry);

    // Verify the outcome
    assertThat(memoAccount.getBalance(), equalTo(0L));

  }

}
