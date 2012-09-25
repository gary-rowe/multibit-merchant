package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.CartItem;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemField;
import org.multibit.mbm.util.DateUtils;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 *  <ul>
 *  <li>Provision of client-side state for a CartItem</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartItemResponse {

  @JsonProperty
  private Long id;

  @JsonProperty
  private String title;

  @JsonProperty
  private String summary;

  @JsonProperty
  private String imgThumbnailUri;

  @JsonProperty
  private int quantity;

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

  @JsonProperty
  private String uom = "each";

  /**
   * Default constructor to allow request building
   */
  public CartItemResponse() {
  }

  /**
   * TODO Widen the mandatory fields to include pricing, stock status and delivery
   *
   * @param cartItem The Item
   */
  public CartItemResponse(CartItem cartItem) {
    Item item = cartItem.getItem();
    this.id = item.getId();
    this.title = item.getItemFieldContent(ItemField.TITLE);
    this.summary = item.getItemFieldContent(ItemField.SUMMARY);
    this.imgThumbnailUri = item.getItemFieldContent(ItemField.IMAGE_THUMBNAIL_URI);
    this.quantity = cartItem.getQuantity();
    if (title != null) {
      // TODO Move the "slugifier" into its own class
      this.slug = title
        .replaceAll("\\p{Punct}", "")
        .replaceAll("\\p{Space}", "-")
        .toLowerCase();
    }
  }

  public Long getId() {
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

  public int getQuantity() {
    return quantity;
  }

  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }
}
