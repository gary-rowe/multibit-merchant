package org.multibit.mbm.client.interfaces.rest.api.adapters.item.usecases;

import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.domain.model.model.ItemField;
import org.multibit.mbm.client.domain.model.model.ItemFieldDetail;
import org.multibit.mbm.client.interfaces.common.DomainAdapter;
import org.multibit.mbm.client.interfaces.rest.api.item.ItemDto;

import java.util.Map;

/**
 * <p>Adapter to provide the following to {@link ItemDto}:</p>
 * <ul>
 * <li>Mapping between DTO and domain objects</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CopyItem implements DomainAdapter<ItemDto, Item> {

  @Override
  public ItemDto on(Item item) {
    ItemDto dto = new ItemDto();

    dto.setId(item.getId().toString());

    dto.setGTIN(item.getGTIN());
    dto.setSKU(item.getSKU());

    dto.setPrice(item.getLocalPrice().toString());
    dto.setTaxRate(String.valueOf(item.getTaxRate()));

    for (Map.Entry<ItemField, ItemFieldDetail> entry: item.getItemFieldMap().entrySet()) {

      String name = entry.getKey().getPropertyNameSingular();
      String value = entry.getValue().getPrimaryDetail().getContent();

      dto.getOptionalProperties().put(name,value);

    }

    return dto;
  }
}
