package org.multibit.mbm.web.mvc;

import org.multibit.mbm.domain.Customer;
import org.multibit.mbm.service.CustomerService;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.security.Principal;

/**
 *  <p>Base class to provide the following to MVC Controllers:<br>
 *  <ul>
 *  <li></li>
 *  </ul>
 *  Example:<br>
 *  <pre>
 *  </pre>
 *  </p>
 *  
 */
public class BaseMVCController {
  @Resource
  private CustomerService customerService = null;

  /**
   * Provides a default handler for injecting customer details into the page model
   *
   * @param principal The security principal
   * @param model     The page model (not null)
   */
  protected void addCustomerToModel(Principal principal, Model model) {
    // TODO Intoduce the obvious refactoring for a base class and "customer proxy object" for model

    // Retrieve the Customer to form the model (if they are authenticated then they will be present)
    Customer customer = customerService.getCustomerFromPrincipal(principal);

    if (customer == null || customer.getEmailAddress() == null) {
      model.addAttribute("greeting", "Welcome!");
      model.addAttribute("emailAddress", "Anonymous");
    } else {
      model.addAttribute("greeting", "Welcome back, "+customer.getEmailAddress());
      model.addAttribute("emailAddress", customer.getEmailAddress());
    }
  }
}
