package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.core.model.Item;
import org.multibit.mbm.core.model.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.core.model.Item}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.core.model.Item} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminItemBridge extends BaseBridge<Item> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.core.model.User} to provide a security principal
   */
  public AdminItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Item item) {

    ResourceAsserts.assertNotNull(item, "item");
    ResourceAsserts.assertNotNull(item.getId(),"id");

    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/item/" + item.getId())
      // Must use individual property entries due to collections
      .withProperty("sku", item.getSKU())
      .withProperty("gtin", item.getGTIN());

  }

}
