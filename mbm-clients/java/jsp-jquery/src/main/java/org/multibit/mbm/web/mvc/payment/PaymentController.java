package org.multibit.mbm.web.mvc.payment;

import org.multibit.mbm.web.mvc.BaseMVCController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * <p>Restricted Controller for Payment page</p>
 *
 * @since 1.0.0
 */
@Controller
public class PaymentController extends BaseMVCController {

  @RequestMapping("/payment.html")
  public String getDeliveryView(Model model, Principal principal) {
    addCustomerToModel(principal, model);
    return "payment/index";
  }

}
