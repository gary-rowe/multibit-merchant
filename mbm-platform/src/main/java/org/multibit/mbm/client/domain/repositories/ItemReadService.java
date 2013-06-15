package org.multibit.mbm.client.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.domain.repositories.common.EntityReadService;

public interface ItemReadService extends EntityReadService<Item> {

  /**
   * Attempt to locate the item using it's SKU
   *
   * @param sku The item SKU
   *
   * @return A matching Item
   */
  Optional<Item> getBySKU(String sku);

  /**
   * Attempt to locate the item using it's GTIN
   *
   * @param gtin The item GTIN
   *
   * @return A matching Item
   */
  Optional<Item> getByGTIN(String gtin);

  /**
   * Persist the given Item
   *
   * @param item A Item (either new or updated)
   *
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
