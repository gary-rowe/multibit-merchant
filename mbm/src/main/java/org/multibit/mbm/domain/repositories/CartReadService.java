package org.multibit.mbm.domain.repositories;

import com.google.common.base.Optional;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Cart;
import org.multibit.mbm.domain.model.model.Customer;
import org.multibit.mbm.domain.repositories.common.EntityReadService;

public interface CartReadService extends EntityReadService<Cart> {

  /**
   * Attempt to locate the Cart
   *
   * @param id The ID
   *
   * @return A matching Cart
   */
  Optional<Cart> getById(Long id);

  /**
   * Get the Cart for the Customer, initialising the {@link org.multibit.mbm.domain.model.model.CartItem}s
   *
   * @param customer The owning Customer
   *
   * @return A persistent cart (never null)
   */
  Optional<Cart> getInitialisedCartByCustomer(Customer customer);

  /**
   * Provide a {@link org.multibit.mbm.domain.common.pagination.PaginatedList} list of all Carts
   *
   * @param pageSize   the total record in one page
   * @param pageNumber the page number starts from 0
   */
  PaginatedList<Cart> getPaginatedList(final int pageSize, final int pageNumber);

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
