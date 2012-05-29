package org.multibit.mbm.api.request;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonProperty;
import org.multibit.mbm.api.response.CartItemResponse;

import java.util.List;

/**
 *  <p>Request to provide the following to {@link org.multibit.mbm.resources.CartController}:</p>
 *  <ul>
 *  <li>Provision of client state to create a {@link org.multibit.mbm.db.dto.Cart}</li>
 *  </ul>
 *
 * @since 0.0.1
 *         
 */
public class CreateCartRequest extends BaseRequest {

  @JsonProperty
  List<CartItemResponse> cartItemSummaries = Lists.newArrayList();

  public List<CartItemResponse> getCartItemSummaries() {
    return cartItemSummaries;
  }

  public void setCartItemSummaries(List<CartItemResponse> cartItemSummaries) {
    this.cartItemSummaries = cartItemSummaries;
  }
}
