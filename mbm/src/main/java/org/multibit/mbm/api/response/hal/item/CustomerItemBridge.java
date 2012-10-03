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

    if (item.getId() == null) {
      throw new IllegalArgumentException("Cannot respond with a transient Item. Id is null.");
    }

    ResourceFactory resourceFactory = getResourceFactory();

    // Create the slug from the title (if it is present)
    String title = item.getItemFieldContent(ItemField.TITLE);
    String slug = null;
    if (title != null) {
      slug = title
        .replaceAll("\\p{Punct}", "")
        .replaceAll("\\p{Space}", "-")
        .toLowerCase();
    }

    Resource userResource = resourceFactory.newResource("/item/" + item.getId())
      .withProperty("id", item.getId())
      .withProperty("sku", item.getSKU())
      .withProperty("slug", slug)
      // End of build
      ;

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ItemField, ItemFieldDetail> entry : item.getItemFieldMap().entrySet()) {
      // Determine the property
      String propertyName = entry.getKey().getPropertyNameSingular();
      boolean isLink = entry.getKey().isLink();
      ItemFieldDetail itemFieldDetail = entry.getValue();
      LocalisedText primaryDetail = itemFieldDetail.getPrimaryDetail();

      // TODO Consider how i18n will be transmitted
      // Consider filtering on Locale
      if (isLink) {
        userResource.withLink(primaryDetail.getContent(), propertyName);
      } else {
        userResource.withProperty(propertyName, primaryDetail.getContent());
      }
      Set<LocalisedText> secondaryDetails = itemFieldDetail.getSecondaryDetails();
      // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
      int index = 1;
      for (LocalisedText secondaryDetail : secondaryDetails) {
        if (isLink) {
          userResource.withLink(secondaryDetail.getContent(), propertyName + index);
        } else {
          userResource.withProperty(propertyName + index, secondaryDetail.getContent());
        }
        index++;
      }
    }

    return userResource;

  }

}
