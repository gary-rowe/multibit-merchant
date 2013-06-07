package org.multibit.mbm.interfaces.rest.api.response.hal.item;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.Set;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of an Item for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicItemBridge extends BaseBridge<Item> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public PublicItemBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Item item) {
    ResourceAsserts.assertNotNull(item, "item");
    ResourceAsserts.assertNotNull(item.getId(),"id");

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

    // Calculate the price
    // TODO Consider currency choice from preferences
    String price = item.getLocalPrice().getAmount().toPlainString();
    String taxRate = String.valueOf(item.getTaxRate());

    Resource userResource = resourceFactory.newResource("/item/" + item.getSKU())
      .withProperty("sku", item.getSKU())
      .withProperty("gtin", item.getGTIN())
      .withProperty("price", price)
      .withProperty("tax_rate", taxRate)
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
