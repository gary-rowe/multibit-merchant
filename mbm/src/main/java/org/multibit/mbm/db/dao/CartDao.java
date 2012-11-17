package org.multibit.mbm.db.dao;

import com.google.common.base.Optional;
import org.multibit.mbm.core.model.Cart;
import org.multibit.mbm.core.model.Customer;

import java.util.List;

public interface CartDao {

  /**
   * Attempt to locate the Cart
   *
   * @param id The ID
   *
   * @return A matching Cart
   */
  Optional<Cart> getById(Long id);

  /**
   * Get the Cart for the Customer, initialising the {@link org.multibit.mbm.core.model.CartItem}s
   *
   * @param customer The owning Customer
   *
   * @return A persistent cart (never null)
   */
  Optional<Cart> getInitialisedCartByCustomer(Customer customer);

  /**
   * Provide a paged list of all Carts
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  List<Cart> getAllByPage(final int pageSize, final int pageNumber);

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
