package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemField;
import org.multibit.mbm.util.DateUtils;

/**
 * <p>Response to provide the following to {@link org.multibit.mbm.api.SearchResults}:</p>
 * <ul>
 * <li>Minimal fields of an Item suitable for use in a search result list</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
@Deprecated
public class ItemResponse implements SearchResponse {

  @JsonProperty
  private String id;

  @JsonProperty
  private String title;

  @JsonProperty
  private String summary;

  @JsonProperty
  private String imgThumbnailUri;

  @JsonProperty
  private String slug;

  @JsonProperty
  private String offeredDeliveryDate = DateUtils.formatFriendlyDate(DateUtils.nowUtc().plusDays(2));

  @JsonProperty
  private String btcPrice = "3.6";

  @JsonProperty
  private String localPrice = "1.4";

  @JsonProperty
  private String localSymbol = "&euro;";

  /**
   * Default constructor for marshaling
   */
  public ItemResponse() {
  }

  /**
   * Utility constructor
   *
   * @param item The Item
   */
  public ItemResponse(Item item) {
    this.id = item.getId().toString();
    this.title = item.getItemFieldContent(ItemField.TITLE);
    this.summary = item.getItemFieldContent(ItemField.SUMMARY);
    this.imgThumbnailUri = item.getItemFieldContent(ItemField.IMAGE_THUMBNAIL_URI);
    this.slug = title.replaceAll(" ", "-").toLowerCase();
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
