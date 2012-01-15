package org.multibit.mbm.accounting;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionTest {

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureLHSNull() {

    // Configure supporting objects
    Account<DefaultEntry> account = new Account<DefaultEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    DefaultEntry d1 = new DefaultEntry(account, 10, then, then);

    new Transaction<DefaultEntry>(null, d1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureRHSNull() {

    // Configure supporting objects
    Account<DefaultEntry> account = new Account<DefaultEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    DefaultEntry w1 = new DefaultEntry(account, -10, then, then);

    new Transaction<DefaultEntry>(w1, null);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureUnbalanced() {

    // Configure supporting objects
    Account<DefaultEntry> account1 = new Account<DefaultEntry>();
    Account<DefaultEntry> account2 = new Account<DefaultEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    DefaultEntry w1 = new DefaultEntry(account1, -10, then, then);
    DefaultEntry d1 = new DefaultEntry(account2, 15, then, then);

    new Transaction<DefaultEntry>(w1, d1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidationFailureSameAccount() {

    // Configure supporting objects
    Account<DefaultEntry> account1 = new Account<DefaultEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    DefaultEntry w1 = new DefaultEntry(account1, -10, then, then);
    DefaultEntry d1 = new DefaultEntry(account1, 15, then, then);

    new Transaction<DefaultEntry>(w1, d1);

  }

  @Test
  public void testBalanceAfterAdd() {

    // Configure supporting objects
    Account<DefaultEntry> account1 = new Account<DefaultEntry>();
    Account<DefaultEntry> account2 = new Account<DefaultEntry>();
    DateTime then = new DateTime(2000, 1, 2, 12, 34, 56, 0);

    // Create a deposit and withdrawal pair
    DefaultEntry w1 = new DefaultEntry(account1, -10, then, then);
    DefaultEntry d1 = new DefaultEntry(account2, 10, then, then);

    Transaction<DefaultEntry> tx = new Transaction<DefaultEntry>(w1, d1);
    tx.commit();

    assertThat(account1.getBalance(), equalTo(-10L));
    assertThat(account2.getBalance(), equalTo(10L));

  }

}
