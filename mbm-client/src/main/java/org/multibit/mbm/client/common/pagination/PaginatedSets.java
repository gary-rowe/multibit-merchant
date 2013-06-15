package org.multibit.mbm.client.common.pagination;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Utility to provide the following to applications:</p>
 * <ul>
 * <li>Easy construction of generic paginated sets</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PaginatedSets {

  /**
   * Provide a paginated set based on an {@link java.util.HashSet}
   *
   * @param pageNumber The current page number
   * @param totalPages The total pages of the collection from which this list is a sub-list
   * @param items      The items to use to populate the set
   * @param <T>        The generic type
   *
   * @return An {@link java.util.ArrayList} decorated with pagination meta data
   */
  public static <T> PaginatedHashSet<T> newPaginatedHashSet(int pageNumber, int totalPages, Set<T> items) {
    HashSet<T> set = Sets.newHashSet(items);

    return new PaginatedHashSet<T>(pageNumber, totalPages, set);

  }
}
