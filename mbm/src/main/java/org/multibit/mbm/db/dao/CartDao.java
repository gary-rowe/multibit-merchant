package org.multibit.mbm.db.dao;

import com.google.common.base.Optional;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.Customer;

public interface CartDao {

  /**
   * Attempt to locate the Cart using it's ID
   *
   * @param id The cart ID
   *
   * @return A matching Cart
   */
  Optional<Cart> getCartById(Long id);

  /**
   * Get the Cart for the Customer, initialising the {@link org.multibit.mbm.db.dto.CartItem}s
   *
   * @param customer The owning Customer
   *
   * @return A persistent cart (never null)
   */
  Optional<Cart> getInitialisedCartByCustomer(Customer customer);


  /**
   * Persist the given Cart
   *
   * @param cart A Cart (either new or updated)
   *
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
