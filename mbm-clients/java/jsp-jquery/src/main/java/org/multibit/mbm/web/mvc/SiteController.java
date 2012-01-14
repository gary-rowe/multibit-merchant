package org.multibit.mbm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * <p>Controller for public site pages</p>
 * <p>Each entry is explicit to avoid letting random page entries resolve</p>
 *
 * @since 1.0.0
 */
@Controller
public class SiteController extends BaseMVCController {

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

}
