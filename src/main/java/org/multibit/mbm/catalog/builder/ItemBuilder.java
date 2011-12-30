package org.multibit.mbm.catalog.builder;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.dto.ItemFieldDetail;
import org.multibit.mbm.i18n.LocalisedText;

/**
 *  <p>Builder to provide the following to {@link Item}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class ItemBuilder {

  private Item item=null;

  /**
   * @return A new instance of the builder
   */
  public static ItemBuilder getInstance() {
    return new ItemBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public Item build() {
    return item;
  }

  /**
   * Create a new {@link Item}
   * @return The builder
   */
  public ItemBuilder newItem() {
    this.item = new Item();
    return this;
  }


  /**
   * 
   * @param itemField The item field (e.g. SUMMARY)
   * @param localeKey The locale key (e.g. "en")
   * @param content The locale specific content (e.g. "Hello", "Bonjour" etc)
   * @return The builder
   */
  public ItemBuilder addPrimaryFieldDetail(ItemField itemField, String localeKey, String content) {
    if (item==null) {
      throw new IllegalStateException("Item is null, need to call newItem() first.");
    }
    LocalisedText localisedText  = new LocalisedText();
    localisedText.setLocaleKey(localeKey);
    localisedText.setContent(content);

    ItemFieldDetail itemFieldDetail = item.getItemFieldDetail(itemField);
    if (itemFieldDetail==null) {
      itemFieldDetail = new ItemFieldDetail();
    }
    itemFieldDetail.setPrimaryDetail(localisedText);

    item.setItemFieldDetail(itemField,itemFieldDetail);

    return this;  
  }

  /**
   *
   * @param itemField The item field (e.g. SUMMARY)
   * @param localeKey The locale key (e.g. "en")
   * @param content The locale specific content (e.g. "Hello", "Bonjour" etc)
   * @return The builder
   */
  public ItemBuilder addSecondaryFieldDetail(ItemField itemField, String localeKey, String content) {
    if (item==null) {
      throw new IllegalStateException("Item is null, need to call newItem() first.");
    }
    LocalisedText localisedText  = new LocalisedText();
    localisedText.setLocaleKey(localeKey);
    localisedText.setContent(content);

    ItemFieldDetail itemFieldDetail = item.getItemFieldDetail(itemField);
    if (itemFieldDetail==null) {
      itemFieldDetail = new ItemFieldDetail();
    }
    itemFieldDetail.getSecondaryDetails().add(localisedText);

    item.setItemFieldDetail(itemField,itemFieldDetail);

    return this;
  }


}
