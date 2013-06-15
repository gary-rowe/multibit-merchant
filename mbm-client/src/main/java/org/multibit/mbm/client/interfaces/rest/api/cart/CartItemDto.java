package org.multibit.mbm.client.interfaces.rest.api.cart;

import org.multibit.mbm.client.common.Identifiable;
import org.multibit.mbm.client.interfaces.rest.api.item.ItemDto;

/**
 * <p>Value object to provide the following to resources:</p>
 * <ul>
 * <li>Binds the {@link CartDto} to the {@link ItemDto}</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CartItemDto implements Identifiable {

  private int quantity;
  private int index;

  private String priceSubtotal;
  private String taxSubtotal;
  private String cartItemSubtotal;

  private ItemDto item;

  @Override
  public Long getId() {
    return (long) getIndex();
  }

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

  public ItemDto getItem() {
    return item;
  }

  public void setItem(ItemDto item) {
    this.item = item;
  }

}
