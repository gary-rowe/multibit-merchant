package org.multibit.mbm.api.response;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Response to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 * <ul>
 * <li>Provides the contents of a Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartResponse extends BaseResponse {

  @JsonProperty
  private String id = "";

  @JsonProperty
  private List<CartItemResponse> cartItems = Lists.newArrayList();

  @JsonProperty
  private String btcTotal = "";

  @JsonProperty
  private String localTotal = "";

  @JsonProperty
  private String localSymbol = "&euro;";

  /**
   * Default constructor
   */
  public CartResponse() {
  }

  /**
   * @param cart The Cart to base the summary upon
   */
  public CartResponse(Cart cart) {

    if (cart.getId() == null) {
      throw new IllegalArgumentException("Cannot respond with a transient Cart. Id is null.");
    }
    id = String.valueOf(cart.getId());

    // Populate the items (keeping a running total)
    double btcSubTotal = 0;
    double localSubTotal = 0;
    for (CartItem cartItem : cart.getCartItems()) {
      cartItems.add(new CartItemResponse(cartItem));
      btcSubTotal += (cartItem.getQuantity() * 36);
      localSubTotal += (cartItem.getQuantity() * 14);
    }

    // Provide a simple totalling system (needs a lot more work!)
    btcTotal = String.format("%.4f", btcSubTotal / 10);
    localTotal = String.format("%.2f", localSubTotal / 10);

    // Provide a simple ordering scheme
    Collections.sort(cartItems, new Comparator<CartItemResponse>() {
      @Override
      public int compare(CartItemResponse c1, CartItemResponse c2) {
        return c1.getTitle().compareTo(c2.getTitle());
      }
    });
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<CartItemResponse> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItemResponse> cartItems) {
    this.cartItems = cartItems;
  }

  public String getBtcTotal() {
    return btcTotal;
  }

  public void setBtcTotal(String btcTotal) {
    this.btcTotal = btcTotal;
  }

  public String getLocalSymbol() {
    return localSymbol;
  }

  public void setLocalSymbol(String localSymbol) {
    this.localSymbol = localSymbol;
  }

  public String getLocalTotal() {
    return localTotal;
  }

  public void setLocalTotal(String localTotal) {
    this.localTotal = localTotal;
  }
}
