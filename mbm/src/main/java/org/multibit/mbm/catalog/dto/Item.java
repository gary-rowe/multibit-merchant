package org.multibit.mbm.catalog.dto;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;
import org.multibit.mbm.cart.dto.CartItem;
import org.multibit.mbm.i18n.dto.LocalisedText;
import org.multibit.mbm.util.ObjectUtils;

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
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  @Column(name = "sku", nullable = false)
  private String sku = null;

  @Column(name = "gtin", nullable = true)
  private String gtin = null;

  @OneToMany(targetEntity=CartItem.class, cascade = {CascadeType.ALL}, mappedBy = "primaryKey.item")
  @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
  private Set<CartItem> cartItems = Sets.newLinkedHashSet();
  
  /**
   * This collection is effectively the fields for the Item so must be eager
   */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @MapKeyEnumerated
  private Map<ItemField, ItemFieldDetail> itemFieldMap = Maps.newLinkedHashMap();

  
  /*
   * Default constructor required for Hibernate
   */
  public Item() {
  }

  /**
   * Required
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
   * @return The global trade item number (see http://en.wikipedia.org/wiki/Global_Trade_Item_Number)
   */
  public String getGTIN() {
    return gtin;
  }

  public void setGTIN(String gtin) {
    this.gtin = gtin;
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
