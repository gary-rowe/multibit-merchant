package org.multibit.mbm.web.rest.v1.catalog;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.util.DateUtils;
import org.multibit.mbm.web.rest.v1.search.SearchSummary;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.search.SearchResults}:</p>
 *  <ul>
 *  <li>Minimal fields of an Item suitable for use in a search result list</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class ItemSearchSummary implements SearchSummary{
  private final String id;
  private final String title;
  private final String summary;
  private final String imgThumbnailUri;
  private String slug;
  private String offeredDeliveryDate=DateUtils.formatFriendlyDate(DateUtils.nowUtc().plusDays(2));
  private String btcPrice="3.6";
  private String localPrice="1.4";
  private String localSymbol="&euro;";

  /**
   * TODO Widen the mandatory fields to include pricing, stock and delivery
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
