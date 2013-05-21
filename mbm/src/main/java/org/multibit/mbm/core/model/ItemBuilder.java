package org.multibit.mbm.core.model;

import com.google.common.collect.Lists;
import com.xeiam.xchange.currency.MoneyUtils;
import org.joda.money.BigMoney;

import java.util.List;

/**
 *  <p>Builder to provide the following to {@link Item}:</p>
 *  <ul>
 *  <li>Provide a fluent interface to facilitate building the entity</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class ItemBuilder {

  private List<PrimaryFieldDetail> primaryFieldDetails = Lists.newArrayList();
  private List<SecondaryFieldDetail> secondaryFieldDetails = Lists.newArrayList();

  private boolean isBuilt = false;
  private String sku = null;
  private String gtin = null;
  private BigMoney localPrice = MoneyUtils.parseBitcoin("BTC 0.0000");

  /**
   * @return A new instance of the builder
   */
  public static ItemBuilder newInstance() {
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

    item.setSKU(sku);

    item.setGTIN(gtin);

    item.setLocalPrice(localPrice);

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
   *
   * @param itemField The item field (e.g. SUMMARY)
   * @param content   The locale specific content (e.g. "Hello", "Bonjour" etc)
   *
   * @param localeKey The locale key (e.g. "en")
   * @return The builder
   */
  public ItemBuilder withPrimaryFieldDetail(ItemField itemField, String content, String localeKey) {

    validateState();

    primaryFieldDetails.add(new PrimaryFieldDetail(itemField, localeKey, content));

    return this;
  }


  /**
   *
   * @param itemField The item field (e.g. SUMMARY)
   * @param content   The locale specific content (e.g. "Hello", "Bonjour" etc)
   *
   * @param localeKey The locale key (e.g. "en")
   * @return The builder
   */
  public ItemBuilder withSecondaryFieldDetail(ItemField itemField, String content, String localeKey) {

    validateState();

    secondaryFieldDetails.add(new SecondaryFieldDetail(itemField, localeKey, content));

    return this;
  }

  /**
   * @param sku The Stock Keeping Unit
   * @return The Builder
   */
  public ItemBuilder withSKU(String sku) {
    this.sku = sku;
    return this;
  }

  /**
   * @param gtin The Global Trade Item Number
   * @return The Builder
   */
  public ItemBuilder withGTIN(String gtin) {
    this.gtin = gtin;
    return this;
  }

  /**
   * @param localPrice The price in the local currency
   * @return The Builder
   */
  public ItemBuilder withLocalPrice(BigMoney localPrice) {
    this.localPrice = localPrice;
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
      itemFieldDetail.setItemField(itemField);
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
      itemFieldDetail.setItemField(itemField);

    }
  }

}
