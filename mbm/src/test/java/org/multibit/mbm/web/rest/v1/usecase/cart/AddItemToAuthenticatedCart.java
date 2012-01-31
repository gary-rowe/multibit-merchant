package org.multibit.mbm.web.rest.v1.usecase.cart;

import org.multibit.mbm.cart.builder.CartBuilder;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.cart.dto.CartItem;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.web.rest.v1.client.cart.CartItemSummary;
import org.multibit.mbm.web.rest.v1.client.cart.CartResponse;
import org.multibit.mbm.web.rest.v1.client.cart.CreateCartRequest;
import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Adds an Item to an authenticated cart</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class AddItemToAuthenticatedCart extends BaseUseCase {

  @Override
  protected void doConfiguration(Map<UseCaseParameter, Object> useCaseParameterMap) {
    // Configure the RestTemplate to use Basic authentication
    useCaseParameterMap.put(UseCaseParameter.HTTP_AUTHENTICATE_BASIC,new String[] {"alice","alice"});
  }

  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {

    // Prepare client side objects
    
    // Simulate a search result offering an ID
    Item book1 = new Item();
    book1.setId(1L);

    // Build a transient Cart referencing the Item
    Cart cart = CartBuilder.getInstance()
      .addCartItem(book1,1)
      .build();

    // Use the transient cart to build the request
    CreateCartRequest createCartRequest = new CreateCartRequest();

    Iterator<CartItem> cartItemIterator = cart.getCartItems().iterator();
    CartItemSummary cartItem1 = new CartItemSummary(cartItemIterator.next());
    createCartRequest.getCartItemSummaries().add(cartItem1);
    createCartRequest.setSessionId("alice123");

    // Perform the test
    
    // Build a POST with attached CartResponse
    CartResponse responseCartResponse = restTemplate.postForObject(
      buildResourceUri("/cart"),
      createCartRequest,
      CartResponse.class);

    assertThat("Unexpected response for POST /cart[item.id=1, quantity=1]", responseCartResponse.getCartItemSummaries().get(0).getQuantity(), equalTo(1));

  }

}
