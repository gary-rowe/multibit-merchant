package org.multibit.mbm.web.mvc;

import org.springframework.ui.Model;

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

  /**
   * Provides a default handler for injecting customer details into the page model
   *
   * @param principal The security principal
   * @param model     The page model (not null)
   */
  protected void addCustomerToModel(Principal principal, Model model) {
    // No customer so use anonymous
    configureAnonymous(model);
  }

  private void configureAnonymous(Model model) {
    model.addAttribute("greeting", "Welcome!");
    model.addAttribute("emailAddress", "Anonymous");
  }
}
