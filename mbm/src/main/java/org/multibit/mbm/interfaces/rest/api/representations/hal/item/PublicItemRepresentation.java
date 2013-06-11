package org.multibit.mbm.interfaces.rest.api.representations.hal.item;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.ItemField;
import org.multibit.mbm.domain.model.model.ItemFieldDetail;
import org.multibit.mbm.domain.model.model.LocalisedText;

import java.util.Map;
import java.util.Set;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Item}:</p>
 * <ul>
 * <li>Creates representations of an Item for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicItemRepresentation {

  public Representation get(Item item) {
    Preconditions.checkNotNull(item, "item");
    Preconditions.checkNotNull(item.getId(), "id");

    RepresentationFactory factory = new DefaultRepresentationFactory();

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

    Representation userRepresentation = factory
      .newRepresentation("/item/" + item.getSKU())
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
        userRepresentation.withLink(primaryDetail.getContent(), propertyName);
      } else {
        userRepresentation.withProperty(propertyName, primaryDetail.getContent());
      }
      Set<LocalisedText> secondaryDetails = itemFieldDetail.getSecondaryDetails();
      // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
      int index = 1;
      for (LocalisedText secondaryDetail : secondaryDetails) {
        if (isLink) {
          userRepresentation.withLink(secondaryDetail.getContent(), propertyName + index);
        } else {
          userRepresentation.withProperty(propertyName + index, secondaryDetail.getContent());
        }
        index++;
      }
    }

    return userRepresentation;

  }

}
