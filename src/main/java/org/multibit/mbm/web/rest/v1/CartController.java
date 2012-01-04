package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.web.rest.v1.cart.CartItem;
import org.multibit.mbm.web.rest.v1.cart.CartSummary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Handles the RESTful APIs for the shopping cart package
 */
@Controller
@RequestMapping(value = "/v1")
public class CartController {

  @Resource(name = "catalogService")
  private CatalogService catalogService = null;

  /**
   * Add an Item to the shopping cart
   * Consider support for named parameters (e.g. category:, reference: etc)
   * Consider caching strategies (e.g. DAO against ItemFieldDetail with word index)
   * Consider a Lucene or Hibernate Search implementation
   *
   *
   * @param id The id to locate
   *
   * @return A batch of matching results
   */
  @RequestMapping(value = "/cart/item/{id}", method = RequestMethod.POST)
  @ResponseBody
  public CartSummary addToCart(@PathVariable(value = "id") Long id) {

    // Broad search of front page items
    Item item = catalogService.getById(id);

    // TODO Consider error handling in API
    if (item==null) {
      return null;
    }

    // TODO Bind this to the customer in the database
    CartSummary cartSummary = new CartSummary();
    cartSummary.getCartItems().add(new CartItem(item));

    return cartSummary;

  }

}

