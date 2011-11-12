package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1")
public class CustomerEntityController {

  /**
   * Echo service
   *
   * @param emailAddress The email address to put into the {@link Customer}
   *
   * @return A populated Customer
   */
  @RequestMapping(value = "/customer/echo/{emailAddress}", method = RequestMethod.GET)
  @ResponseBody
  public Customer alert(@PathVariable("emailAddress") String emailAddress) {
    Customer customer = new Customer();
    customer.setEmailAddress(emailAddress);
    return customer;
  }
}

