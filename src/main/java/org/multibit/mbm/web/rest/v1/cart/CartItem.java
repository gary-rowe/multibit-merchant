package org.multibit.mbm.web.rest.v1.cart;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.util.DateUtils;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.CartController}:</p>
 *  <ul>
 *  <li>Provision of detailed state information for a Cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CartItem {

  private final String id;
  private final String title;
  private final String summary;
  private final String imgThumbnailUri;
  private String slug;
  private String offeredDeliveryDate= DateUtils.formatFriendlyDate(DateUtils.nowUtc().plusDays(2));
  private String btcPrice="3.6";
  private String localPrice="1.4";
  private String localSymbol="&euro;";
  private String quantity="1";

  /**
   * TODO Widen the mandatory fields to include pricing, stock and delivery
   * @param item The Item
   */
  public CartItem(Item item) {
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

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }
}
