package org.multibit.mbm.interfaces.rest.api.adapters.item;

import com.google.common.base.Preconditions;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.interfaces.rest.api.adapters.item.usecases.CopyItem;
import org.multibit.mbm.interfaces.rest.api.item.ItemDto;

/**
 * <p>Adapter to provide the following to {@link ItemDto}:</p>
 * <ul>
 * <li>Mapping between DTO and domain objects</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ItemAdapters {


  public static ItemDto copyItem(Item item) {
    Preconditions.checkNotNull(item, "'item' cannot be null");

    return new CopyItem().on(item);

  }

}
