package org.multibit.mbm.web.rest.v1.client.search;

/**
 *  <p>Value object to provide the following to RESTful controllers:</p>
 *  <ul>
 *  <li>Standard search result handling</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */

import com.google.common.collect.Lists;
import org.multibit.mbm.web.rest.v1.client.catalog.ItemSummary;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Represents the results of a generic search
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(ItemSummary.class)
public class SearchResults<T extends SearchResponse> {
  
  @XmlElementWrapper(name = "results")
  @XmlElement(name = "item", type = ItemSummary.class)
  private List<T> results= Lists.newArrayList();

  @XmlElement
  private long start=0L;

  @XmlElement
  private boolean lastPage=true;

  /**
   * Default constructor for marshaling
   */
  public SearchResults() {
  }

  /**
   * Utility constructor
   * @param results The results
   * @param start The start position
   * @param lastPage True if this is the last page
   */
  public SearchResults(List<T> results, long start, boolean lastPage) {
    this.results=results;
    this.start=start;
    this.lastPage=lastPage;
  }

  /**
   * @return The list of matching results
   */
  public List<T> getResults() {
    return results;
  }

  /**
   * @return The starting index of the results
   */
  public long getStart() {
    return start;
  }

  /**
   * @return True if this is the last page of results
   */
  public boolean isLastPage() {
    return lastPage;
  }
}
