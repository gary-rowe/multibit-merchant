package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import org.multibit.mbm.db.dao.CartDao;
import org.multibit.mbm.db.dto.Cart;
import org.multibit.mbm.db.dto.CartBuilder;
import org.multibit.mbm.db.dto.Customer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Repository("hibernateCartDao")
public class HibernateCartDao extends BaseHibernateDao implements CartDao {

  @Resource(name = "hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Optional<Cart> getCartById(Long id) {
    Assert.notNull(id, "id cannot be null");
    List carts = hibernateTemplate.find("from Cart c where c.id = ?", id);
    if (isNotFound(carts)) return Optional.absent();

    return Optional.of((Cart) carts.get(0));
  }

  @Override
  public Optional<Cart> getInitialisedCartByCustomer(Customer customer) {
    Assert.notNull(customer, "customer cannot be null");

    Cart cart = customer.getCart();
    if (cart == null) {
      // Create a suitable cart
      cart = CartBuilder.newInstance()
        .build();
      customer.setCart(cart);
    }

    // Ensure we associate the Customer and Cart with the current session and initialise the collection
    hibernateTemplate.saveOrUpdate(customer);
    hibernateTemplate.initialize(customer.getCart().getCartItems());

    return Optional.of(cart);

  }

  @Override
  public Cart saveOrUpdate(Cart cart) {
    Assert.notNull(cart, "cart cannot be null");
    hibernateTemplate.saveOrUpdate(cart);
    return cart;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }

  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
