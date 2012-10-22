package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Item}:</p>
 * <ul>
 * <li>Creates representation of multiple Items for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminItemCollectionBridge extends BaseBridge<List<Item>> {

  private final CustomerItemBridge customerItemBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminItemCollectionBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerItemBridge = new CustomerItemBridge(uriInfo,principal);
  }

  public Resource toResource(List<Item> items) {

    ResourceAsserts.assertNotNull(items, "items");

    ResourceFactory resourceFactory = getResourceFactory();

    Resource itemList = resourceFactory.newResource(uriInfo.getRequestUri().toString());

    for (Item item : items) {
      Resource itemResource = customerItemBridge.toResource(item);

      itemResource.withProperty("id", item.getId())
        // End of build
        ;

      itemList.withSubresource("/item/"+item.getId(), itemResource);
    }

    return itemList;

  }

}
