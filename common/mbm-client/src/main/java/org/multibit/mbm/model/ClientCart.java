package org.multibit.mbm.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>Value object to provide the following to resources:</p>
 * <ul>
 * <li>Storage of state for Cart representations by the client</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientCart {

  private String localSymbol;
  private String localTotal;
  private String btcTotal;
  private String itemTotal;
  private String quantityTotal;
  private List<ClientCartItem> cartItems = Lists.newArrayList();

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

  public String getBtcTotal() {
    return btcTotal;
  }

  public void setBtcTotal(String btcTotal) {
    this.btcTotal = btcTotal;
  }

  /**
   * @return The number of individual items
   */
  public String getItemTotal() {
    return itemTotal;
  }

  public void setItemTotal(String itemTotal) {
    this.itemTotal = itemTotal;
  }

  public List<ClientCartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<ClientCartItem> cartItems) {
    this.cartItems = cartItems;
  }

  /**
   * @return The total number of items
   */
  public String getQuantityTotal() {
    return quantityTotal;
  }

  public void setQuantityTotal(String quantityTotal) {
    this.quantityTotal = quantityTotal;
  }

  /**
   * @return True if the cart items are empty (Freemarker requirement)
   */
  public boolean isEmpty() {
    return cartItems.isEmpty();
  }
}
