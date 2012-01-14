package org.multibit.mbm.accounting;

/**
 * <p>Rule to provide the following to {@link Account}:</p>
 * <ul>
 * <li>Provision of application and reversal methods</li>
 * </ul>
 * <p>A PostingRule operates on a given Account and typically creates a new
 * Entry representing the application of the rule.</p>
 * <p>A PostingRule is reversible, such that given an entry it will provide
 * an opposite entry to any previously created from the same entry.</p>
 * <p>For example, if a DetailAccount in response to an Entry being
 * added should create an Entry in a MemoAccount of a certain
 * fraction of the original Entry then a PostingRule would be used
 * to accomplish this.</p>
 *
 * @since 1.0.0
 *         
 */
public interface PostingRule<T extends Entry<T>> {
  /**
   * @param sourceEntry Apply this rule to the source entry
   */
  void apply(T sourceEntry);

  /**
   * @param sourceEntry Reverse this rule
   */
  void reverse(T sourceEntry);
}
