package org.multibit.mbm.model;

/**
 * <p>Value object to provide the following to resources:</p>
 * <ul>
 * <li>Binds the ClientCart to the ClientItem </li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientCartItem {

  private int quantity;
  private int index;

  private String priceSubtotal;
  private String taxSubtotal;
  private String cartItemSubtotal;

  private ClientItem item;

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getPriceSubtotal() {
    return priceSubtotal;
  }

  public void setPriceSubtotal(String priceSubtotal) {
    this.priceSubtotal = priceSubtotal;
  }

  public String getTaxSubtotal() {
    return taxSubtotal;
  }

  public void setTaxSubtotal(String taxSubtotal) {
    this.taxSubtotal = taxSubtotal;
  }

  public String getCartItemSubtotal() {
    return cartItemSubtotal;
  }

  public void setCartItemSubtotal(String cartItemSubtotal) {
    this.cartItemSubtotal = cartItemSubtotal;
  }

  public ClientItem getItem() {
    return item;
  }

  public void setItem(ClientItem item) {
    this.item = item;
  }
}
