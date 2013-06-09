package org.multibit.mbm.interfaces.rest.api.cart;

import com.google.common.collect.Lists;
import org.multibit.mbm.domain.common.Identifiable;

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
public class CartDto implements Identifiable {

  private Long id;
  private String currencySymbol;
  private String currencyCode;
  private String priceTotal;
  private String itemTotal;
  private String quantityTotal;
  private List<CartItemDto> cartItems = Lists.newArrayList();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getPriceTotal() {
    return priceTotal;
  }

  public void setPriceTotal(String priceTotal) {
    this.priceTotal = priceTotal;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
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

  public List<CartItemDto> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItemDto> cartItems) {
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
