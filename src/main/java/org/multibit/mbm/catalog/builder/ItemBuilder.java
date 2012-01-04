package org.multibit.mbm.catalog.builder;

import com.google.common.collect.Lists;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.dto.ItemFieldDetail;
import org.multibit.mbm.i18n.dto.LocalisedText;

import java.util.List;

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

  private List<PrimaryFieldDetail> primaryFieldDetails = Lists.newArrayList();
  private List<SecondaryFieldDetail> secondaryFieldDetails = Lists.newArrayList();

  private boolean isBuilt = false;
  private String sku = null;
  private String gtin = null;

  /**
   * @return A new instance of the builder
   */
  public static ItemBuilder getInstance() {
    return new ItemBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   *
   * @return The item instance
   */
  public Item build() {

    validateState();

    // Item is a DTO so requires a public default constructor
    Item item = new Item();

    if (sku == null) {
      throw new IllegalStateException("SKU is a mandatory field");
    }
    item.setSKU(sku);

    item.setGTIN(gtin);

    for (PrimaryFieldDetail primaryFieldDetail : primaryFieldDetails) {
      primaryFieldDetail.applyTo(item);
    }
    for (SecondaryFieldDetail secondaryFieldDetail : secondaryFieldDetails) {
      secondaryFieldDetail.applyTo(item);
    }

    isBuilt = true;

    return item;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("Build process is complete - no further changes can be made");
    }
  }

  /**
   * @param itemField The item field (e.g. SUMMARY)
   * @param localeKey The locale key (e.g. "en")
   * @param content   The locale specific content (e.g. "Hello", "Bonjour" etc)
   *
   * @return The builder
   */
  public ItemBuilder addPrimaryFieldDetail(ItemField itemField, String localeKey, String content) {

    validateState();

    primaryFieldDetails.add(new PrimaryFieldDetail(itemField, localeKey, content));

    return this;
  }


  /**
   * @param itemField The item field (e.g. SUMMARY)
   * @param localeKey The locale key (e.g. "en")
   * @param content   The locale specific content (e.g. "Hello", "Bonjour" etc)
   *
   * @return The builder
   */
  public ItemBuilder addSecondaryFieldDetail(ItemField itemField, String localeKey, String content) {

    validateState();

    secondaryFieldDetails.add(new SecondaryFieldDetail(itemField, localeKey, content));

    return this;
  }

  public ItemBuilder setSKU(String sku) {
    this.sku = sku;
    return this;
  }

  public ItemBuilder setGTIN(String gtin) {
    this.gtin = gtin;
    return this;
  }


  /**
   * Storage of parameters until ready for application
   */
  private class PrimaryFieldDetail {
    private final ItemField itemField;
    private final String localeKey;
    private final String content;

    PrimaryFieldDetail(ItemField itemField, String localeKey, String content) {
      this.itemField = itemField;
      this.localeKey = localeKey;
      this.content = content;
    }

    /**
     * Applies the parameters to the given Item
     *
     * @param item The Item
     */
    void applyTo(Item item) {
      LocalisedText localisedText = new LocalisedText();
      localisedText.setLocaleKey(localeKey);
      localisedText.setContent(content);

      ItemFieldDetail itemFieldDetail = item.getItemFieldDetail(itemField);
      if (itemFieldDetail == null) {
        itemFieldDetail = new ItemFieldDetail();
      }
      itemFieldDetail.setPrimaryDetail(localisedText);

      item.setItemFieldDetail(itemField, itemFieldDetail);

    }
  }

  /**
   * Storage of parameters until ready for application
   */
  private class SecondaryFieldDetail {
    private final ItemField itemField;
    private final String localeKey;
    private final String content;

    SecondaryFieldDetail(ItemField itemField, String localeKey, String content) {
      this.itemField = itemField;
      this.localeKey = localeKey;
      this.content = content;
    }

    /**
     * Applies the parameters to the given Item
     *
     * @param item The item
     */
    void applyTo(Item item) {
      LocalisedText localisedText = new LocalisedText();
      localisedText.setLocaleKey(localeKey);
      localisedText.setContent(content);

      ItemFieldDetail itemFieldDetail = item.getItemFieldDetail(itemField);
      if (itemFieldDetail == null) {
        itemFieldDetail = new ItemFieldDetail();
      }
      itemFieldDetail.getSecondaryDetails().add(localisedText);

      item.setItemFieldDetail(itemField, itemFieldDetail);

    }
  }

}
