package org.multibit.mbm.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class AccountService {

  @RequestMapping("/account.html")
  public String getView(Model model, Principal principal) {

    // TODO Hook into the authentication database
    model.addAttribute("friendlyName", principal.getName());
    return "account/index";
  }
}
