package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.customer.service.CustomerService;
import org.multibit.mbm.util.CookieUtils;
import org.multibit.mbm.web.rest.v1.cart.CartSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
   * Add an Item to the shopping cart
   * Consider support for named parameters (e.g. category:, reference: etc)
   * Consider caching strategies (e.g. DAO against ItemFieldDetail with word index)
   * Consider a Lucene or Hibernate Search implementation
   *
   * @param itemId    The Item ID
   * @param principal The current security principal (injected)
   * @param request   The original request (injected)
   *
   * @return A batch of matching results
   */
  @RequestMapping(value = "/cart/item/{itemId}", method = RequestMethod.POST)
  @ResponseBody
  public CartSummary addToCart(@PathVariable(value = "itemId") Long itemId, @RequestParam(value = "token", required = false) String uuid, Principal principal, HttpServletRequest request) {

    // TODO Generalise this behaviour (possibly into the CustomerController)
    // Ensure that a limit on the creation of Customers from a given IP address is enforced
    Customer customer = null;
    if (principal == null || principal instanceof AnonymousAuthenticationToken) {
      log.debug("Looking for UUID in cookie");
      // Require a UUID to proceed
      if (uuid == null) {
        // Attempt to locate it on a Cookie
        uuid = CookieUtils.getCookieValue(request, TokenController.TOKEN_COOKIE_NAME);
        if (uuid == null) {
          // No token anywhere
          throw new UUIDNotFoundException();
        }
      }
      log.debug("Persisting anonymous Customer with UUID '{}'",uuid);
      customer = customerService.persistAnonymousCustomer(uuid);
    } else {
      log.debug("Persisting authenticated Customer");
      customer = customerService.getCustomerByPrincipal(principal);
    }
    
    customer = customerService.addItemToCart(customer, itemId, 1);

    return new CartSummary(customer.getCart());

  }

  public void setCatalogService(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}

