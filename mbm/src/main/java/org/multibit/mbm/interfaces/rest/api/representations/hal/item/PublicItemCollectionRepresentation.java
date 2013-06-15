package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representation of multiple Items for the public</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicItemCollectionRepresentation {

  private final PublicItemRepresentation publicItemRepresentation = new PublicItemRepresentation();

  public Representation get(PaginatedList<Item> items) {

    Preconditions.checkNotNull(items, "items");

    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation itemList = factory.newRepresentation();

    // Use the reduced public fields as embedded resources
    for (Item item : items.list()) {
      Representation itemRepresentation = publicItemRepresentation.get(item);
      itemList.withRepresentation("item", itemRepresentation);
    }

    return itemList;

  }

}
