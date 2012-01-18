package org.multibit.mbm.catalog.dao;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.web.rest.v1.catalog.ItemPagedQuery;

import java.util.List;

public interface ItemDao {

  /**
   * Attempt to locate the item using it's ID
   *
   * @param id The item ID
   * @return A matching Item (never null)
   * @throws org.multibit.mbm.catalog.dao.ItemNotFoundException If the item is not matched
   */
  Item getById(Long id) throws ItemNotFoundException;

  /**
   * Attempt to locate the item using it's SKU
   * @param sku The item SKU
   * @return A matching Item
   */
  Item getBySKU(String sku);

  /**
   * Attempt to locate the item using it's sku
   * @param gtin The item GTIN
   * @return A matching Item
   */
  Item getByGTIN(String gtin);

  /**
   * @param itemPagedQuery The query parameters
   * @return The matching items, or an empty list (never null)
   */
  List<Item> getPagedItems(ItemPagedQuery itemPagedQuery);

  /**
   * Persist the given Item
   * @param item A Item (either new or updated)
   * @return The persisted Item
   */
  Item saveOrUpdate(Item item);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
