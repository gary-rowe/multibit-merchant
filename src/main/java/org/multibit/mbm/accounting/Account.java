package org.multibit.mbm.accounting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>Account to provide the following to service layers:</p>
 * <ul>
 * <li>Transactional collection of entries</li>
 * <li>Notification of PostingRules</li>
 * </ul>
 * <p>An Account represents a collection of Entries </p>
 *
 * @since 1.0.0
 *         
 */
public class Account<T extends Entry<T>> {

  /**
   * The logger for this class
   */
  private static final Logger log = LoggerFactory.getLogger(Account.class);

  /**
   * The account name (optional)
   */
  private String name=null;

  /**
   * The account description (optional)
   */
  private String description=null;

  /**
   * A description of the underlying item type unit of measurement (optional)
   */
  private String unitOfMeasurement=null;

  /**
   * The set of entries
   */
  private Set<T> entrySet = new LinkedHashSet<T>();

  /**
   * The set of posting rules (trigger on new entries)
   */
  private Set<PostingRule<T>> postingRuleSet = new LinkedHashSet<PostingRule<T>>();

  /**
   * The account balance
   */
  private long balance = 0L;

  /**
   * Recalculates the overall balance based on the entries
   */
  protected void recalculate() {
    balance = 0L;
    for (T entry : entrySet) {
      balance += entry.getAmount();
    }
    log.debug("Balance recalculated as {}",balance);
  }

  /**
   * Notifies the PostingRules that an Entry has been submitted
   * @param entry The original Entry
   */
  private void notifyPostingRules(T entry) {
    for (PostingRule<T> postingRule: postingRuleSet) {
      postingRule.apply(entry);
    }
    log.debug("Posting rules notified");
  }

  /**
   * @return The sum of all quantities within the entries
   */
  public long getBalance() {
    return balance;
  }

  /**
   * @return The Set of all Entry instances
   */
  public Set<T> getEntrySet() {
    return entrySet;
  }

  /**
   * Entries should be added by a Transaction
   * @param entry A single Entry
   */
  protected void addEntry(T entry) {
    this.entrySet.add(entry);
    balance += entry.getAmount();
    notifyPostingRules(entry);
  }

  /**
   * Entries should be added by a Transaction
   * @param entries A collection of entries
   */
  protected void addEntrySet(Set<T> entries) {
    this.entrySet.addAll(entries);
    recalculate();
  }

  /**
   * @return The PostingRules for this Account
   */
  public Set<PostingRule<T>> getPostingRuleSet() {
    return postingRuleSet;
  }

  /**
   * @param postingRule A PostingRule
   */
  public void addPostingRule(PostingRule<T> postingRule) {
    this.postingRuleSet.add(postingRule);
  }

  /**
   * @param postingRuleSet A Set of PostingRules for this Account
   */
  public void addPostingRuleSet(Set<PostingRule<T>> postingRuleSet) {
    this.postingRuleSet.addAll(postingRuleSet);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUnitOfMeasurement() {
    return unitOfMeasurement;
  }

  public void setUnitOfMeasurement(String unitOfMeasurement) {
    this.unitOfMeasurement = unitOfMeasurement;
  }
}
