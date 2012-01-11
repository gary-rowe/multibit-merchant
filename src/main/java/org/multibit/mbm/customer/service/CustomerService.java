package org.multibit.mbm.customer.service;

import org.multibit.mbm.cart.dao.CartDao;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.customer.builder.CustomerBuilder;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.customer.dao.CustomerNotFoundException;
import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.util.OpenIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

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

  private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

  @Resource(name = "hibernateCustomerDao")
  private CustomerDao customerDao;

  @Resource(name = "hibernateCartDao")
  private CartDao cartDao;

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  /**
   * <p>Attempts to insert new Customers into the database in response to a successful login</p>
   *
   * @param openId The OpenId that uniquely identifies the Customer
   *
   * @return The authenticated Customer
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer persistAuthenticatedCustomer(String openId) {
    try {
      // Is this a new customer?
      return customerDao.getCustomerByOpenId(openId);
    } catch (CustomerNotFoundException ex) {
      log.debug("Inserting new authenticated customer");
      String uuid = UUID.randomUUID().toString();
      Customer newCustomer = CustomerBuilder.getInstance()
        .setOpenId(openId)
        .setUUID(uuid)
        .build();
      return customerDao.saveOrUpdate(newCustomer);
    }

  }

  /**
   * <p>Attempts to insert new Customers into the database in response to a successful UUID presentation</p>
   *
   * @param uuid The UUID that uniquely identifies the Customer
   *
   * @return The anonymous Customer
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer persistAnonymousCustomer(String uuid) {
    try {
      // Is this a new customer?
      return customerDao.getCustomerByUUID(uuid);
    } catch (CustomerNotFoundException ex) {
      log.debug("Inserting new anonymous customer");
      Customer newCustomer = CustomerBuilder.getInstance()
        .setUUID(uuid)
        .build();
      return customerDao.saveOrUpdate(newCustomer);
    }
  }


  /**
   * <p>Attempt to locate the Customer using the security Principal</p>
   *
   * @param principal The security principal (if null then null is returned)
   *
   * @return A Customer or null if not found
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer getCustomerByPrincipal(Principal principal) {
    Customer customer = null;
    String emailAddress = null;

    if (principal == null) {
      log.warn("Null principal passed in.");
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
        customer = customerDao.saveOrUpdate(customer);
      }
    }

    return customer;
  }

  /**
   * Handles the process of updating the quantity of CartItems
   *
   * @param customer The Customer
   * @param itemId   The Item ID
   * @param quantity The quantity to set
   *
   * @return The updated Customer
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer setCartItemQuantity(Customer customer, Long itemId, int quantity) {
    Cart cart = cartDao.getInitialisedCartByCustomer(customer);

    // Find the Item in the database
    Item item = itemDao.getById(itemId);

    // Set the quantity of Items in the cart
    cart.setItemQuantity(item, quantity);

    return customerDao.saveOrUpdate(customer);

  }

  /**
   * A
   *
   * @param uuid The UUID
   *
   * @return The Customer
   */
  public Customer getCustomerByUUID(String uuid) {
    return customerDao.getCustomerByUUID(uuid);
  }

  /**
   * Attempts to persist the Customer
   *
   * @param customer The Customer to persist
   *
   * @return The Customer
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Customer persist(Customer customer) {
    return customerDao.saveOrUpdate(customer);
  }

  /**
   * Package local to allow testing
   *
   * @return The Customer DAO
   */
  CustomerDao getCustomerDao() {
    return customerDao;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public void setCartDao(CartDao cartDao) {
    this.cartDao = cartDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
