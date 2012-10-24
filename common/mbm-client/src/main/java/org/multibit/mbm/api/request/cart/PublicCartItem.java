package org.multibit.mbm.api.request.cart;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *  <p>Value object to provide the following to {@link PublicUpdateCartRequest}:</p>
 *  <ul>
 *  <li>Defines the updates to the CartItem</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicCartItem {

  @JsonProperty
  private Long id;

  @JsonProperty
  private int quantity;

  /**
   * Default constructor to allow request building
   */
  public PublicCartItem() {
  }

  /**
   * Utility constructor for mandatory fields
   *
   * @param itemId   The Item id
   * @param quantity The quantity required
   */
  public PublicCartItem(Long itemId, int quantity) {
    this.id = itemId;
    this.quantity = quantity;
  }

  /**
   * @return The Item ID
   */
  public Long getId() {
    return id;
  }

  /**
   * @return The unit quantity
   */
  public int getQuantity() {
    return quantity;
  }
}
