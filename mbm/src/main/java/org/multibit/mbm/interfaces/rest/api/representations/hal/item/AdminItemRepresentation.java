package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Item;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.domain.model.model.Item} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminItemRepresentation {

  public Representation get(Item item) {

    Preconditions.checkNotNull(item, "item");
    Preconditions.checkNotNull(item.getId(), "id");

    RepresentationFactory factory = new DefaultRepresentationFactory();

    return factory.newRepresentation("/item/" + item.getId())
      // Must use individual property entries due to collections
      .withProperty("sku", item.getSKU())
      .withProperty("gtin", item.getGTIN());

  }

}
