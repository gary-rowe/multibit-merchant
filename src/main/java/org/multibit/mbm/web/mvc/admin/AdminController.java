package org.multibit.mbm.web.mvc.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
/**
 * <p>Restricted Controller for Admin page</p>
 *
 * @since 1.0.0
 */
@Controller
public class AdminController {

  @RequestMapping("/admin.html")
  public String getView(Model model, Principal principal) {

    return "admin/index";
  }

}
