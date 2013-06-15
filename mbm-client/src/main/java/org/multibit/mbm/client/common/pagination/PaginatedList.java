package org.multibit.mbm.client.common.pagination;

import java.util.List;

/**
 * <p>Interface to provide the following to domain:</p>
 * <ul>
 * <li>Common methods to support pagination</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public interface PaginatedList<T> {

  /**
   * @return The pagination information
   */
  Pagination pagination();

  /**
   * @return The underlying {@link List}
   */
  List<T> list();

}
