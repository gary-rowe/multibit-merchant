package org.multibit.mbm.web.rest.v1.client.cart;

import com.google.common.collect.Lists;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.cart.dto.CartItem;
import org.multibit.mbm.web.rest.v1.client.BaseResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  <p>Response to provide the following to {@link org.multibit.mbm.web.rest.v1.controller.CartController}:</p>
 *  <ul>
 *  <li>Provides the contents of a Cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CartResponse extends BaseResponse {

  private List<CartItemSummary> cartItems = Lists.newArrayList();
  private String btcTotal = "";
  private String localTotal = "";
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
    // Populate the items (keeping a running total)
    double btcSubTotal = 0;
    double localSubTotal = 0;
    for (CartItem cartItem : cart.getCartItems()) {
      cartItems.add(new CartItemSummary(cartItem));
      btcSubTotal += (cartItem.getQuantity() * 36);
      localSubTotal += (cartItem.getQuantity() * 14);
    }
    // Provide a simple totalling system (needs a lot more work!)
    btcTotal = String.format("%.4f",btcSubTotal/10);
    localTotal = String.format("%.2f",localSubTotal/10);

    // Provide a simple ordering scheme
    Collections.sort(cartItems, new Comparator<CartItemSummary>() {
      @Override
      public int compare(CartItemSummary c1, CartItemSummary c2) {
        return c1.getTitle().compareTo(c2.getTitle());
      }
    });
  }

  public List<CartItemSummary> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItemSummary> cartItems) {
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
