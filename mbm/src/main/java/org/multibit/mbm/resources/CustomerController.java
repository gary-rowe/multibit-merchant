package org.multibit.mbm.resources;

import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Handles the RESTful APIs for the {@link Customer}s
 */
@Controller
@RequestMapping(value = "/v1")
@Deprecated
public class CustomerController {

  Customer customer;

  @Resource(name="customerService")
  private CustomerService customerService=null;

}

