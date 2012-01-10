package org.multibit.mbm.cart.dao;

import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.customer.dao.CustomerNotFoundException;
import org.multibit.mbm.customer.dto.Customer;

public interface CartDao {

  /**
   * Attempt to locate the Cart using it's ID
   *
   * @param id The cart ID
   * @return A matching Cart
   * @throws org.multibit.mbm.cart.dao.CartNotFoundException If something goes wrong
   */
  Cart getCartById(Long id) throws CartNotFoundException;

  /**
   * Get the Cart for the Customer, initialising the {@link org.multibit.mbm.cart.dto.CartItem}s
   * 
   * @param customer The owning Customer
   * @return A persistent cart (never null)
   * @throws org.multibit.mbm.customer.dao.CustomerNotFoundException If the Customer is not found
   */
  Cart getInitialisedCartByCustomer(Customer customer) throws CustomerNotFoundException;

  
  /**
   * Persist the given Cart
   * @param cart A Cart (either new or updated)
   * @return The persisted Cart
   */
  Cart saveOrUpdate(Cart cart);

  /**
   * <p>Force an immediate in-transaction flush</p>
   * <p>Normally, this is only used in test code but must be on the interface to ensure
   * that injection works as expected</p>
   */
  void flush();

}
