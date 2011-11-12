package org.multibit.mbm.accounting;

/**
 *  <p>Factory to provide the following to Accounting subsystem:<br>
 *  <ul>
 *  <li>Provision of transient Account structures suitable for different purposes</li>
 *  </ul>
 *  </p>
 *  
 */
public enum AccountingPracticeFactory {

  INSTANCE;

  public Account<DefaultEntry> buildDefaultCustomerOrderAccount() {
    // TODO Fill this in
    return new Account<DefaultEntry>();
  }

}
