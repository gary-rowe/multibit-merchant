package org.multibit.mbm.web.rest.v1.catalog;

import java.util.Locale;

/**
 * Parameter object to provide query state for Items
 */
public class ItemPagedQuery {
  private final int firstResult;
  private final int maxResults;
  private final String title;
  private final String summary;
  private final String localeKey;

  /**
   * REFACTOR to use builder inner class
   * TODO Add support for ordering through a comparator
   *
   * @param firstResult The first result position (0-based)
   * @param maxResults The max results limit
   * @param title The title field text to match 
   * @param summary The summary field text to match 
   * @param localeKey The localeKey (null implies using default)
   */
  public ItemPagedQuery(int firstResult, int maxResults, String title, String summary, String localeKey) {
    this.firstResult = firstResult;
    this.maxResults = maxResults;
    this.title = title;
    this.summary = summary;
    this.localeKey = localeKey == null ? Locale.getDefault().toString() : localeKey;
  }

  public int getFirstResult() {
    return firstResult;
  }

  public int getMaxResults() {
    return maxResults;
  }

  public String getTitle() {
    return title;
  }

  public String getSummary() {
    return summary;
  }

  public String getLocaleKey() {
    return localeKey;
  }
}
