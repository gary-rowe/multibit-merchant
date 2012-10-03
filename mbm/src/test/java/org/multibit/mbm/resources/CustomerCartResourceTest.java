package org.multibit.mbm.resources;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dto.*;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

public class CustomerCartResourceTest extends BaseJerseyResourceTest {

  private final CustomerCartResource testObject=new CustomerCartResource();


  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the Customer User for authenticated access
    User customerUser = setUpAuthenticator(Lists.newArrayList(customerRole));
    customerUser.setId(1L);

    Customer customer = customerUser.getCustomer();
    customer.setId(1L);

    // Configure the Cart with Items
    Cart customerCart = customer.getCart();
    customerCart.setId(1L);

    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);

    CartItem cartItem1 = new CartItem(customerCart, book1);
    cartItem1.setQuantity(1);
    CartItem cartItem2 = new CartItem(customerCart, book2);
    cartItem1.setQuantity(2);

    customerCart.getCartItems().add(cartItem1);
    customerCart.getCartItems().add(cartItem2);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void customerRetrieveCartAsHalJson() throws Exception {

    String actualResponse = client()
      .resource("/cart")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Customer retrieve Cart as HAL+JSON", actualResponse, "fixtures/hal/cart/expected-customer-retrieve-cart.json");

  }


}
