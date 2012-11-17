package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.core.model.Item;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.core.model.Item}:</p>
 * <ul>
 * <li>Creates representation of multiple Items for the public</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicItemCollectionBridge extends BaseBridge<List<Item>> {

  private final PublicItemBridge publicItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.core.model.User} to provide a security principal
   */
  public PublicItemCollectionBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    publicItemBridge = new PublicItemBridge(uriInfo,principal);
  }

  public Resource toResource(List<Item> items) {

    ResourceAsserts.assertNotNull(items, "items");

    ResourceFactory resourceFactory = getResourceFactory();

    Resource itemList = resourceFactory.newResource(uriInfo.getRequestUri().toString());

    // Use the reduced public fields as embedded resources
    for (Item item : items) {
      Resource itemResource = publicItemBridge.toResource(item);
      itemList.withSubresource("item", itemResource);
    }

    return itemList;

  }

}
