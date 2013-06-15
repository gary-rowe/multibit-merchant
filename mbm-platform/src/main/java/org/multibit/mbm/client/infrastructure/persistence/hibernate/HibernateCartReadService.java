package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.repositories.CartReadService;
import org.multibit.mbm.client.domain.model.model.Cart;
import org.multibit.mbm.client.domain.model.model.CartBuilder;
import org.multibit.mbm.client.domain.model.model.Customer;
import org.springframework.stereotype.Repository;

@Repository("hibernateCartDao")
public class HibernateCartReadService extends BaseHibernateReadService<Cart> implements CartReadService {

  @SuppressWarnings("unchecked")
  @Override
  public Optional<Cart> getById(Long id) {
    return getById(Cart.class, id);
  }

  @Override
  public Optional<Cart> getInitialisedCartByCustomer(Customer customer) {
    Preconditions.checkNotNull(customer, "customer cannot be null");

    Cart cart = customer.getCart();
    if (cart == null) {
      // Create a suitable cart
      cart = CartBuilder.newInstance()
        .build();
      customer.setCart(cart);
    }

    // Ensure we associate the Customer and Cart with the current session and initialise the collection
    hibernateTemplate.saveOrUpdate(customer);
    return Optional.of(initialized(cart));
  }


  @Override
  protected Cart initialized(Cart entity) {
    hibernateTemplate.initialize(entity.getCartItems());
    return entity;
  }

  @SuppressWarnings("unchecked")
  public PaginatedList<Cart> getPaginatedList(final int pageSize, final int pageNumber) {
    return buildPaginatedList(pageSize, pageNumber, Cart.class);
  }

  @Override
  public Cart saveOrUpdate(Cart cart) {
    Preconditions.checkNotNull(cart, "cart cannot be null");
    hibernateTemplate.saveOrUpdate(cart);
    return cart;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }

}
