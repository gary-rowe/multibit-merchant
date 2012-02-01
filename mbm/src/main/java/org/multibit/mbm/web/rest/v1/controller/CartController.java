package org.multibit.mbm.web.rest.v1.controller;

import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.customer.service.CustomerService;
import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.web.rest.v1.client.cart.CartItemSummary;
import org.multibit.mbm.web.rest.v1.client.cart.CartResponse;
import org.multibit.mbm.web.rest.v1.client.cart.CreateCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Handles the RESTful APIs for the shopping cart package
 */
@Controller
@RequestMapping(value = "/v1")
public class CartController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(CartController.class);

  @Resource(name = "catalogService")
  private CatalogService catalogService = null;

  @Resource(name = "customerService")
  private CustomerService customerService = null;


  /**
   * Retrieves the  Cart for the authenticated User
   * @param principal The security principal (injected)
   * @param request The originating request (injected)
   * @return The response
   */
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/cart"
  )
  @ResponseBody
  public CartResponse retrieveCart(
    Principal principal,
    HttpServletRequest request) {

    User user = getUserFromRequest(principal, request, null);

    // Validate the expected form of the data
    Assert.notNull(user);
    Assert.notNull(user.getCustomer());

    return new CartResponse(user.getCustomer().getCart());
  }

  /**
   * Creates a new Cart for the User
   * @param createCartRequest The initial Cart contents
   * @param principal The security principal (injected)
   * @param request The originating request (injected)
   * @param response The response (injected)
   * @return The response
   */
  @RequestMapping(
    headers = {"content-type=application/json"},
    method = RequestMethod.POST,
    value = "/cart"
  )
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public CartResponse createCart(
    @RequestBody CreateCartRequest createCartRequest, 
    Principal principal, 
    HttpServletRequest request, 
    HttpServletResponse response ) {

    // TODO Validate the request
    
    String sessionId = createCartRequest.getSessionId();

    User user = getUserFromRequest(principal, request, sessionId);
    
    Customer customer = user.getCustomer();
    
    for (CartItemSummary cartItemSummary: createCartRequest.getCartItemSummaries()) {
      Long itemId = cartItemSummary.getId();
      int quantity = cartItemSummary.getQuantity();
      customer = customerService.setCartItemQuantity(customer, itemId, quantity);
    }

    // Ensure the new Cart can be found (one Cart per User)
    addLocationHeader("/cart",request,response);

    // Provide the entire Cart as a shortcut for lazy clients
    return new CartResponse(customer.getCart());

  }

  public void setCatalogService(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}

