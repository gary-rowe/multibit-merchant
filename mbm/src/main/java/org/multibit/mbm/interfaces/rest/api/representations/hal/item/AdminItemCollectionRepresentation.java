package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Item;

import java.util.List;

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

  public Representation get(List<Item> items) {

    Preconditions.checkNotNull(items, "items");

    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation itemList = factory.newRepresentation();

    for (Item item : items) {
      Representation itemRepresentation = customerItemRepresentation.get(item);

      itemRepresentation.withProperty("id", item.getId())
        // End of build
        ;

      itemList.withRepresentation("/item/" + item.getId(), itemRepresentation);
    }

    return itemList;

  }

}
