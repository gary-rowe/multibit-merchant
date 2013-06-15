package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.interfaces.rest.api.hal.Representations;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representation of multiple Items for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminItemCollectionRepresentation {

  private final PublicItemRepresentation customerItemRepresentation = new PublicItemRepresentation();

  public Representation get(PaginatedList<Item> items) {

    Preconditions.checkNotNull(items, "items");

    URI self = UriBuilder.fromPath("/admin/item").build();
    Representation itemList = Representations.newPaginatedList(self, items);

    for (Item item : items.list()) {
      Representation itemRepresentation = customerItemRepresentation.get(item);

      itemRepresentation.withProperty("id", item.getId())
        // End of build
        ;

      itemList.withRepresentation("/item/" + item.getId(), itemRepresentation);
    }

    return itemList;

  }

}
