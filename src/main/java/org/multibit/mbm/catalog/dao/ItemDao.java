package org.multibit.mbm.catalog.dao;

import org.multibit.mbm.catalog.dao.ItemNotFoundException;
import org.multibit.mbm.catalog.dto.Item;

public interface ItemDao {

  /**
   * Attempt to locate the item using it's reference
   * @param reference The item reference
   * @return A matching Item
   * @throws org.multibit.mbm.catalog.dao.ItemNotFoundException If something goes wrong
   */
  Item getItemByReference(String reference) throws ItemNotFoundException;

  /**
   * Persist the given Item
   * @param item A Item (either new or updated)
   */
  Item persist(Item item);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
