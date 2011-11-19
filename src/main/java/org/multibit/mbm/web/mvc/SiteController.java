package org.multibit.mbm.web.mvc;

import org.multibit.mbm.domain.Customer;
import org.multibit.mbm.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * <p>Controller for public site pages</p>
 * <p>Each entry is explicit to avoid letting random page entries resolve</p>
 *
 * @since 1.0.0
 */
@Controller
public class SiteController {

  @Resource
  private CustomerService customerService = null;

  @RequestMapping("/authenticate.html")
  public String getAuthenticateView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "public/authenticate";
  }

  @RequestMapping("/index.html")
  public String getIndexView(Model model, Principal principal) {

    addCustomerToModel(principal, model);
    return "public/index";
  }

  @RequestMapping("/contact.html")
  public String getContactView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "public/contact";
  }

  @RequestMapping("/delivery.html")
  public String getDeliveryView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "public/delivery";
  }

  @RequestMapping("/feedback.html")
  public String getFeedbackView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "public/feedback";
  }

  @RequestMapping("/help.html")
  public String getHelpView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "public/help";
  }

  /**
   * Provides a default handler for injecting customer details into the page model
   *
   * @param principal The security principal
   * @param model     The page model (not null)
   */
  private void addCustomerToModel(Principal principal, Model model) {
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
