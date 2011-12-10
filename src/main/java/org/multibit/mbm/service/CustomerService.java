package org.multibit.mbm.service;

import org.multibit.mbm.customer.ContactMethod;
import org.multibit.mbm.customer.ContactMethodDetail;
import org.multibit.mbm.customer.Customer;
import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.util.OpenIdUtils;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

/**
 * <p>Service to provide the following to Controllers:</p>
 * <ul>
 * <li>Transactional collection of Customer entries</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
@Service
@Transactional(readOnly = true)
public class CustomerService {

  @Resource(name = "hibernateCustomerDao")
  private CustomerDao customerDao;

  /**
   * <p>Attempts to insert new Customers into the database in response to a successful login</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void haveBeenAuthenticated(String openId) {
    try {
      // Is this a new customer?
      customerDao.getCustomerByOpenId(openId);
    } catch (CustomerNotFoundException ex) {
      // Yes, so add them in
      Customer newCustomer = new Customer();
      newCustomer.setOpenId(openId);
      customerDao.persist(newCustomer);
    }

  }

  /**
   * Attempts to locate a Customer based on the given Principal
   * @param principal The security principal (can be null but just leads to a null Customer)
   * @return A Customer or null if not found
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer getCustomerFromPrincipal(Principal principal) {
    Customer customer = null;
    String emailAddress = null;

    if (principal==null) {
      return null;
    }

    if (principal instanceof OpenIDAuthenticationToken) {
      // Extract information from the OpenId principal
      OpenIDAuthenticationToken token = (OpenIDAuthenticationToken) principal;
      String openId = token.getIdentityUrl();
      List<String> values = OpenIdUtils.getAttributeValuesByName((OpenIDAuthenticationToken) principal, "email");
      if (!values.isEmpty()) {
        emailAddress = values.get(0);
      }

      // Attempt to locate the customer
      customer = customerDao.getCustomerByOpenId(openId);

      // Check for known primary email address (if supplied)
      ContactMethodDetail contactMethodDetail = customer.getContactMethodDetail(ContactMethod.EMAIL);
      if (contactMethodDetail != null && contactMethodDetail.getPrimaryDetail() == null && emailAddress != null) {
        // Fill in the obtained email address
        contactMethodDetail.setPrimaryDetail(emailAddress);
        customerDao.persist(customer);
      }
    } else {
      // Use standard username/password lookup
      // TODO Requires supporting infrastructure
    }

    return customer;
  }

  /**
   * Package private to allow testing
   * @return The Customer DAO
   */
  CustomerDao getCustomerDao() {
    return customerDao;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }
}
