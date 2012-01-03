package org.multibit.mbm.web.rest.v1.catalog;

import org.multibit.mbm.catalog.dto.Item;

/**
 *  <p>value object to provide the following to {@link org.multibit.mbm.web.rest.v1.CatalogController}:</p>
 *  <ul>
 *  <li>Provision of detailed state information for an {@link org.multibit.mbm.catalog.dto.Item}</li>
 *  </ul>
 *  Example:<br>
 *  <pre>
 *  </pre>
 *
 * @since 1.0.0
 *         
 */
public class ItemDetail extends ItemSearchSummary {

  public ItemDetail(Item item) {
    super(item);
  }
}
