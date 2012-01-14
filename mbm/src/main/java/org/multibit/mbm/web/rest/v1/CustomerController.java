package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.customer.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Handles the RESTful APIs for the Customer package
 */
@Controller
@RequestMapping(value = "/v1")
public class CustomerController {

  @Resource(name="customerService")
  private CustomerService customerService=null;
  
  /**
   * Echo service
   *
   * @param emailAddress The email address to put into the {@link org.multibit.mbm.customer.dto.Customer}
   *
   * @return A populated Customer
   */
  @RequestMapping(value = "/customer/echo/{emailAddress}", method = RequestMethod.GET)
  @ResponseBody
  public Customer alert(@PathVariable("emailAddress") String emailAddress) {
    Customer customer = new Customer();
    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail(emailAddress);
    customer.setContactMethodDetail(ContactMethod.EMAIL,contactMethodDetail);
    return customer;
  }
}

