package org.multibit.mbm.customer.service;

import org.multibit.mbm.cart.dao.CartDao;
import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.customer.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
