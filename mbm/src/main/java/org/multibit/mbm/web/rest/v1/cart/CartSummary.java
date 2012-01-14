package org.multibit.mbm.web.rest.v1.cart;

import com.google.common.collect.Lists;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.cart.dto.CartItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.CartController}:</p>
 *  <ul>
 *  <li>Provision of state information for the contents of a Cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CartSummary {

  private List<CartItemSummary> cartItemSummaries = Lists.newArrayList();
  private String btcTotal = "";
  private String localTotal = "";
  private String localSymbol = "&euro;";

  public CartSummary(Cart cart) {
    // Populate the items (keeping a running total)
    double btcSubTotal = 0;
    double localSubTotal = 0;
    for (CartItem cartItem : cart.getCartItems()) {
      cartItemSummaries.add(new CartItemSummary(cartItem));
      btcSubTotal += (cartItem.getQuantity() * 36);
      localSubTotal += (cartItem.getQuantity() * 14);
    }
    // Provide a simple totalling system (needs a lot more work!)
    btcTotal = String.format("%.4f",btcSubTotal/10);
    localTotal = String.format("%.2f",localSubTotal/10);

    // Provide a simple ordering scheme
    Collections.sort(cartItemSummaries, new Comparator<CartItemSummary>() {
      @Override
      public int compare(CartItemSummary c1, CartItemSummary c2) {
        return c1.getTitle().compareTo(c2.getTitle());
      }
    });
  }

  public List<CartItemSummary> getCartItemSummaries() {
    return cartItemSummaries;
  }

  public void setCartItemSummaries(List<CartItemSummary> cartItemSummaries) {
    this.cartItemSummaries = cartItemSummaries;
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
