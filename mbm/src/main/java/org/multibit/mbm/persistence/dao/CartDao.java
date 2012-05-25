package org.multibit.mbm.persistence.dao;

import org.multibit.mbm.persistence.dto.Cart;
import org.multibit.mbm.persistence.dto.Customer;

public interface CartDao {

  /**
   * Attempt to locate the Cart using it's ID
   *
   * @param id The cart ID
   * @return A matching Cart
   * @throws CartNotFoundException If something goes wrong
   */
  Cart getCartById(Long id) throws CartNotFoundException;

  /**
   * Get the Cart for the Customer, initialising the {@link org.multibit.mbm.persistence.dto.CartItem}s
   * 
   * @param customer The owning Customer
   * @return A persistent cart (never null)
   * @throws CustomerNotFoundException If the Customer is not found
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
