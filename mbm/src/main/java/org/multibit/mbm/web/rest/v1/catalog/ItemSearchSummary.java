package org.multibit.mbm.web.rest.v1.catalog;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.util.DateUtils;
import org.multibit.mbm.web.rest.v1.search.SearchSummary;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.search.SearchResults}:</p>
 *  <ul>
 *  <li>Minimal fields of an Item suitable for use in a search result list</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSearchSummary implements SearchSummary{

  @XmlElement
  private String id;
  @XmlElement
  private String title;
  @XmlElement
  private String summary;
  @XmlElement
  private String imgThumbnailUri;
  @XmlElement
  private String slug;
  @XmlElement
  private String offeredDeliveryDate=DateUtils.formatFriendlyDate(DateUtils.nowUtc().plusDays(2));
  @XmlElement
  private String btcPrice="3.6";
  @XmlElement
  private String localPrice="1.4";
  @XmlElement
  private String localSymbol="&euro;";

  /**
   * Default constructor for marshaling
   */
  public ItemSearchSummary() {
  }

  /**
   * Utility constructor
   * @param item The Item
   */
  public ItemSearchSummary(Item item) {
    this.id = item.getId().toString();
    this.title = item.getItemFieldContent(ItemField.TITLE);
    this.summary = item.getItemFieldContent(ItemField.SUMMARY);
    this.imgThumbnailUri = item.getItemFieldContent(ItemField.IMAGE_THUMBNAIL_URI);
    this.slug = title.replaceAll(" ","-").toLowerCase();
  }

  public String getId() {
    return id;
  }

  public String getSummary() {
    return summary;
  }

  public String getTitle() {
    return title;
  }

  public String getImgThumbnailUri() {
    return imgThumbnailUri;
  }

  public String getSlug() {
    return slug;
  }

  public String getOfferedDeliveryDate() {
    return offeredDeliveryDate;
  }

  public String getBtcPrice() {
    return btcPrice;
  }

  public String getLocalPrice() {
    return localPrice;
  }

  public String getLocalSymbol() {
    return localSymbol;
  }

}
