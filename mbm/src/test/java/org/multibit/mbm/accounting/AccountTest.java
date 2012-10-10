package org.multibit.mbm.accounting;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountTest {

  @Test
  public void testInitialBalance() {
    Account testObject = new Account();

    assertThat(testObject.getBalance(), equalTo(0L));

  }

  @Test
  public void testBalanceAfterAdd() {
    Account<DefaultEntry> testObject = new Account<DefaultEntry>();

    DefaultEntry e1 = EntryFactory.INSTANCE.buildDefaultEntry(testObject, 0L);
    testObject.addEntry(e1);
    assertThat(testObject.getBalance(), equalTo(0L));

    DefaultEntry e2 = EntryFactory.INSTANCE.buildDefaultEntry(testObject, 1L);
    testObject.addEntry(e2);
    assertThat(testObject.getBalance(), equalTo(1L));

    DefaultEntry e3 = EntryFactory.INSTANCE.buildDefaultEntry(testObject, 2L);
    testObject.addEntry(e3);
    assertThat(testObject.getBalance(), equalTo(3L));

    DefaultEntry e4 = EntryFactory.INSTANCE.buildDefaultEntry(testObject, 3L);
    testObject.addEntry(e4);
    assertThat(testObject.getBalance(), equalTo(6L));

  }

  @Test
  public void testBalanceAfterAddAll() {

    // Configure supporting objects
    Account<DefaultEntry> testObject = new Account<DefaultEntry>();

    Set<DefaultEntry> entries = new HashSet<DefaultEntry>();
    entries.add(EntryFactory.INSTANCE.buildDefaultEntry(testObject, 0L));
    entries.add(EntryFactory.INSTANCE.buildDefaultEntry(testObject, 1L));
    entries.add(EntryFactory.INSTANCE.buildDefaultEntry(testObject, -2L));
    entries.add(EntryFactory.INSTANCE.buildDefaultEntry(testObject, 3L));

    testObject.addEntrySet(entries);
    assertThat(testObject.getBalance(), equalTo(2L));

  }

}
