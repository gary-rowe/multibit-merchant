package org.multibit.mbm.web.mvc;

import org.multibit.mbm.customer.ContactMethod;
import org.multibit.mbm.customer.ContactMethodDetail;
import org.multibit.mbm.customer.Customer;
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
    // TODO Introduce the obvious refactoring for a base class and "customer proxy object" for model

    // Retrieve the Customer to form the model (if they are authenticated then they will be present)
    Customer customer = customerService.getCustomerFromPrincipal(principal);
    if (customer == null) {
      // No customer so use anonymous
      model.addAttribute("greeting", "Welcome!");
      model.addAttribute("emailAddress", "Anonymous");
    } else {
      // Customer is known, so attempt to provide a customised configuration
      ContactMethodDetail contactMethodDetail = customer.getContactMethodDetail(ContactMethod.EMAIL);

      if (contactMethodDetail.getPrimaryDetail() == null) {
        model.addAttribute("greeting", "Welcome!");
        model.addAttribute("emailAddress", "Anonymous");
      } else {
        model.addAttribute("greeting", "Welcome back, " + contactMethodDetail.getPrimaryDetail());
        model.addAttribute("emailAddress", contactMethodDetail.getPrimaryDetail());
      }
    }
  }
}
