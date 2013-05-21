package org.multibit.mbm.core.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeiam.xchange.currency.MoneyUtils;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.BigMoney;
import org.multibit.mbm.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>DTO to provide the following to the application</p>
 * <ul>
 * <li>Provision of persistent state</li>
 * </ul>
 * <p>A Item provides the central link for all the aspects that come together to describe a product for sale.</p>
 */
@Entity
@Table(name = "items")
public class Item implements Serializable {

  private static final long serialVersionUID = 38947590324750L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * <p>The <a href="http://en.wikipedia.org/wiki/Stock-keeping_unit">stock-keeping unit</a></p>
   * <p>Provides a mandatory code to identify an item using a local arbitrary structure, e.g. "ABC-123". The GTIN
   * value could be replicated here if appropriate.</p>
   */
  @Column(name = "sku", nullable = false)
  private String sku = null;

  /**
   * <p>The <a href="http://en.wikipedia.org/wiki/Global_Trade_Item_Number">global trade item number</a></p>
   * <p>Provides the optional GTIN code to identify an item using international standards such as UPC, EAN and IAN.
   * In the case of books, ISBN is compatible with the EAN-13 standard.</p>
   */
  @Column(name = "gtin", nullable = true)
  private String gtin = null;

  /**
   * Indicates if the User has been deleted (archived)
   */
  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  /**
   * Provides a reason for being deleted
   */
  @Column(name = "reasonForDelete", nullable = true)
  private String reasonForDelete = null;

  @OneToMany(
    targetEntity = CartItem.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.item",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  private Set<CartItem> cartItems = Sets.newLinkedHashSet();

  // TODO An Item has many prices depending on date, volume, discount, premium etc
  @Columns(columns = {@Column(name = "amount"), @Column(name = "currency")})
  @Type(type = "org.multibit.mbm.db.dao.hibernate.type.BigMoneyType")
  private BigMoney localPrice = MoneyUtils.parseBitcoin("BTC 0.0000");

  // TODO An Item has many tax rates depending on date, seller etc
  @Column(name = "taxRate", nullable = false)
  private double taxRate = 0.0;

  /**
   * This collection is effectively the fields for the Item so must be eager
   */
  @OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  @MapKeyEnumerated
  private Map<ItemField, ItemFieldDetail> itemFieldMap = Maps.newLinkedHashMap();


  /*
  * Default constructor required for Hibernate
  */
  public Item() {
  }

  /**
   * Required
   *
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Required
   *
   * @return The stock-keeping unit reference (see http://en.wikipedia.org/wiki/Stock-keeping_unit)
   */
  public String getSKU() {
    return sku;
  }

  public void setSKU(String sku) {
    this.sku = sku;
  }

  /**
   * Optional
   *
   * @return The global trade item number (see http://en.wikipedia.org/wiki/Global_Trade_Item_Number)
   */
  public String getGTIN() {
    return gtin;
  }

  public void setGTIN(String gtin) {
    this.gtin = gtin;
  }

  /**
   * @return The tax rate applicable to this Item
   */
  public double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(double taxRate) {
    this.taxRate = taxRate;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getReasonForDelete() {
    return reasonForDelete;
  }

  public void setReasonForDelete(String reasonForDelete) {
    this.reasonForDelete = reasonForDelete;
  }

  /**
   * The CartItem instances that bind Cart and Item<br>
   * Cascade occurs on the owner side
   *
   * @return Returns cartItems
   */
  public Set<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(Set<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  /**
   * @return The price per item in the local currency
   */
  public BigMoney getLocalPrice() {
    return localPrice;
  }

  public void setLocalPrice(BigMoney localPrice) {
    this.localPrice = localPrice;
  }

  /**
   * @return The raw {@link ItemField} map
   */
  public Map<ItemField, ItemFieldDetail> getItemFieldMap() {
    return itemFieldMap;
  }

  public void setItemFieldMap(Map<ItemField, ItemFieldDetail> itemFieldMap) {
    this.itemFieldMap = itemFieldMap;
  }

  /**
   * Utility method to assist locating default entries for a given field
   *
   * @param itemField The item field (e.g. "SUMMARY", "INSTRUCTIONS")
   *
   * @return The localised content for the default locale, or null if it does not exist
   */
  @Transient
  public String getItemFieldContent(ItemField itemField) {
    if (itemFieldMap.containsKey(itemField)) {
      LocalisedText localisedText = itemFieldMap.get(itemField).getPrimaryDetail();
      if (localisedText != null) {
        return localisedText.getContent();
      }
    }
    return null;
  }

  /**
   * Utility method to assist locating default entries for a given locale
   *
   * @param itemField The item field (e.g. "SUMMARY", "INSTRUCTIONS")
   * @param localeKey The locale key (e.g. "en" or "en_US")
   *
   * @return The localised content for the given locale, or null if it does not exist
   */
  @Transient
  public String getItemFieldContent(ItemField itemField, String localeKey) {


    // Get the field detail that contains the primary and secondary content
    ItemFieldDetail itemFieldDetail = getItemFieldDetail(itemField);

    if (itemFieldDetail != null) {
      // Attempt to locate the content for the given locale key

      // Always check the primary field first
      LocalisedText localisedText = itemFieldDetail.getPrimaryDetail();
      if (localisedText != null) {
        if (localisedText.getLocaleKey().equalsIgnoreCase(localeKey)) {
          // The primary detail has a matching locale
          return localisedText.getContent();
        }

        // Primary failed, so examine the secondary detail
        if (!itemFieldDetail.getSecondaryDetails().isEmpty()) {

          for (LocalisedText secondary : itemFieldDetail.getSecondaryDetails()) {
            if (secondary.getLocaleKey().equalsIgnoreCase(localeKey)) {
              // Return the first match
              return secondary.getContent();
            }
          }

        } // No matching content in secondary

      } // No localised text

    } // Parameters are invalid

    return null;
  }


  /**
   * Utility method to assist locating particular entries
   *
   * @param itemField The item field (e.g. "SUMMARY", "INSTRUCTIONS")
   *
   * @return The {@link ItemFieldDetail} providing the information, or null if none available
   */
  @Transient
  public ItemFieldDetail getItemFieldDetail(ItemField itemField) {
    return itemFieldMap.get(itemField);
  }

  /**
   * Utility method to assist locating particular entries
   *
   * @param itemField       The item field (e.g. "SUMMARY", "INSTRUCTIONS")
   * @param itemFieldDetail The contact method details providing the email address, or VOIP address etc
   */
  @Transient
  public void setItemFieldDetail(ItemField itemField, ItemFieldDetail itemFieldDetail) {
    itemFieldMap.put(itemField, itemFieldDetail);
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Item other = (Item) obj;

    return ObjectUtils.isEqual(
      id, other.id,
      sku, other.sku,
      gtin, other.gtin
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id, sku);
  }

  @Override
  public String toString() {
    return String.format("Item[id=%s, SKU='%s', GTIN='%s']]", id, sku, gtin);
  }

}
