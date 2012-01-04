package org.multibit.mbm.web.rest.v1.cart;

import com.google.common.collect.Lists;

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

  private List<CartItem> cartItems = Lists.newArrayList();
  private String btcTotal="3.6";
  private String localTotal="1.4";
  private String localSymbol="&euro;";

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
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
