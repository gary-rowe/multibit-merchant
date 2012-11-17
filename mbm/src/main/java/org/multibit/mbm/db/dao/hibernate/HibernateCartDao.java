package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.core.model.Cart;
import org.multibit.mbm.core.model.CartBuilder;
import org.multibit.mbm.core.model.Customer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernateCartDao")
public class HibernateCartDao extends BaseHibernateDao<Cart> implements CartDao {

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
  public List<Cart> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<Cart>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from Cart");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
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
