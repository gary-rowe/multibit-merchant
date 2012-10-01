package org.multibit.mbm.api.response;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Response to provide the following to {@link org.multibit.mbm.resources.CustomerCartResource}:</p>
 * <ul>
 * <li>Provides the contents of a Cart</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 * TODO Replace with the HAL bridge
 */
@Deprecated
public class CustomerCartResponse extends BaseResponse {

  @JsonProperty
  private String id = "";

  @JsonProperty
  private List<CustomerCartItem> cartItems = Lists.newArrayList();

  @JsonProperty
  private String btcTotal = "";

  @JsonProperty
  private String localTotal = "";

  @JsonProperty
  private String localSymbol = "&euro;";

  /**
   * Default constructor
   */
  public CustomerCartResponse() {
  }

  /**
   * @param cart The Cart to base the summary upon
   */
  public CustomerCartResponse(Cart cart) {

    if (cart.getId() == null) {
      throw new IllegalArgumentException("Cannot respond with a transient Cart. Id is null.");
    }
    id = String.valueOf(cart.getId());

    // Populate the items (keeping a running total)
    double btcSubTotal = 0;
    double localSubTotal = 0;
    for (CartItem cartItem : cart.getCartItems()) {
      cartItems.add(new CustomerCartItem(cartItem));
      btcSubTotal += (cartItem.getQuantity() * 36);
      localSubTotal += (cartItem.getQuantity() * 14);
    }

    // Provide a simple totalling system (needs a lot more work!)
    btcTotal = String.format("%.4f", btcSubTotal / 10);
    localTotal = String.format("%.2f", localSubTotal / 10);

    // Provide a simple ordering scheme
    Collections.sort(cartItems, new Comparator<CustomerCartItem>() {
      @Override
      public int compare(CustomerCartItem c1, CustomerCartItem c2) {
        return c1.getId().compareTo(c2.getId());
      }
    });
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<CustomerCartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CustomerCartItem> cartItems) {
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
