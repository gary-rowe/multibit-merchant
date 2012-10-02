package org.multibit.mbm.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.*;

import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.Set;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Item}:</p>
 * <ul>
 * <li>Creates representations of an Item for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerItemBridge extends BaseBridge<Item> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Item item) {
    ResourceFactory resourceFactory = getResourceFactory();

    Resource userResource = resourceFactory.newResource("/item/" + item.getId())
      .withProperty("id",item.getId())
      .withProperty("sku", item.getSKU())
      // End of build
      ;

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ItemField, ItemFieldDetail> entry : item.getItemFieldMap().entrySet()) {
      // Determine the property
      String propertyName = entry.getKey().getPropertyNameSingular();
      ItemFieldDetail itemFieldDetail = entry.getValue();
      LocalisedText primaryDetail = itemFieldDetail.getPrimaryDetail();
      // TODO Consider l10n
      userResource.withProperty(propertyName, primaryDetail.getContent());

      // Determine if secondary details should be included
      if (entry.getKey().isSecondaryDetailSupported()) {
        Set<String> secondaryDetails = itemFieldDetail.getSecondaryDetails();
        // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
        int index = 1;
        for (String secondaryDetail : secondaryDetails) {
          userResource.withProperty(propertyName + index, secondaryDetail);
          index++;
        }
      }
    }

    return userResource;

  }

}
