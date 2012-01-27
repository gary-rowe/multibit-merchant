package org.multibit.mbm.web.rest.v1.client.cart;

import org.multibit.mbm.cart.dto.CartItem;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.util.DateUtils;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provision of client-side state for a CartItem</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CartItemSummary {

  private Long id;
  private String title;
  private String summary;
  private String imgThumbnailUri;
  private int quantity;
  private String slug;
  private String offeredDeliveryDate = DateUtils.formatFriendlyDate(DateUtils.nowUtc().plusDays(2));
  private String btcPrice = "3.6";
  private String localPrice = "1.4";
  private String localSymbol = "&euro;";
  private String uom = "each";


  /**
   * Default constructor to allow request building
   */
  public CartItemSummary() {
  }

  /**
   * TODO Widen the mandatory fields to include pricing, stock status and delivery
   *
   * @param cartItem The Item
   */
  public CartItemSummary(CartItem cartItem) {
    Item item = cartItem.getItem();
    this.id = item.getId();
    this.title = item.getItemFieldContent(ItemField.TITLE);
    this.summary = item.getItemFieldContent(ItemField.SUMMARY);
    this.imgThumbnailUri = item.getItemFieldContent(ItemField.IMAGE_THUMBNAIL_URI);
    this.quantity = cartItem.getQuantity();
    if (title != null) {
      this.slug = title.replaceAll(" ", "-").toLowerCase();
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
