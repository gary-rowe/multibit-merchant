package org.multibit.mbm.web;

import org.multibit.mbm.domain.Customer;
import org.multibit.mbm.service.CustomerService;
import org.multibit.mbm.util.OpenIdUtils;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

  @Resource
  private CustomerService customerService=null;

  @RequestMapping("/account.html")
  public String getView(Model model, Principal principal) {

    Customer customer = customerService.getCustomerFromOpenId(principal.getName());

    String emailAddress = null;

    // Read any useful information from the principal
    if (principal instanceof OpenIDAuthenticationToken) {
      List<String> values = OpenIdUtils.getAttributeValuesByName((OpenIDAuthenticationToken) principal, "email");
      if (!values.isEmpty()) {
        emailAddress = values.get(0);
      }
    }

    // TODO Set the email address properly during authentication
    customer.setEmailAddress(emailAddress);
    model.addAttribute("emailAddress", customer.getEmailAddress());
    return "account/index";
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}
