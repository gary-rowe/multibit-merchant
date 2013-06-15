package org.multibit.mbm.domain.common.pagination;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Utility to provide the following to applications:</p>
 * <ul>
 * <li>Easy construction of generic paginated lists</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PaginatedLists {

  /**
   * Provide a paginated list based on an {@link ArrayList} using the default results per page
   *
   * @param pageNumber The current page number
   * @param totalPages The total pages of the collection from which this list is a sub-list
   * @param items      The collection of items to use to populate the list
   * @param <T>        The generic type
   *
   * @return An {@link ArrayList} decorated with pagination meta data
   */
  public static <T> PaginatedArrayList<T> newPaginatedArrayList(int pageNumber, int totalPages, Collection<T> items) {
    ArrayList<T> list = Lists.newArrayList(items);

    return new PaginatedArrayList<T>(pageNumber, totalPages, list);

  }

  /**
   * Provide a paginated list based on an {@link ArrayList} using the default results per page
   *
   * @param pageNumber     The current page number
   * @param totalPages     The total pages of the collection from which this list is a sub-list
   * @param resultsPerPage The number of results per page
   * @param items          The collection of items to use to populate the list
   * @param <T>            The generic type
   *
   * @return An {@link ArrayList} decorated with pagination meta data
   */
  public static <T> PaginatedArrayList<T> newPaginatedArrayList(int pageNumber, int totalPages, int resultsPerPage, Collection<T> items) {

    ArrayList<T> list = Lists.newArrayList(items);
    return new PaginatedArrayList<T>(pageNumber, totalPages, resultsPerPage, list);

  }

}
