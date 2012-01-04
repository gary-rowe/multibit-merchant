package org.multibit.mbm.catalog.dao;

import org.multibit.mbm.catalog.dto.Item;

import java.util.List;

public interface ItemDao {

  /**
   * Attempt to locate the item using it's ID
   *
   * @param id The item ID
   * @return A matching Item
   * @throws org.multibit.mbm.catalog.dao.ItemNotFoundException If something goes wrong
   */
  Item getItemById(Long id) throws ItemNotFoundException;

  /**
   * Attempt to locate the item using it's SKU
   * @param sku The item SKU
   * @return A matching Item
   * @throws org.multibit.mbm.catalog.dao.ItemNotFoundException If something goes wrong
   */
  Item getItemBySKU(String sku) throws ItemNotFoundException;

  /**
   * Attempt to locate the item using it's sku
   * @param gtin The item GTIN
   * @return A matching Item
   * @throws org.multibit.mbm.catalog.dao.ItemNotFoundException If something goes wrong
   */
  Item getItemByGTIN(String gtin);

  /**
   * TODO Remove this once the main search solution is in place since it does not scale
   * @return All items
   */
  List<Item> getAllItems();

  /**
   * Persist the given Item
   * @param item A Item (either new or updated)
   * @return The persisted Item
   */
  Item persist(Item item);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
