package org.multibit.mbm.cart.dao;

import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.customer.dao.CustomerNotFoundException;
import org.multibit.mbm.customer.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Repository("hibernateCartDao")
public class HibernateCartDao implements CartDao {

  @Resource(name="hibernateTemplate")
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Cart getCartById(Long id) throws CartNotFoundException {
    Assert.notNull(id, "id cannot be null");
    List carts = hibernateTemplate.find("from Cart c where c.id = ?", id);
    if (carts == null || carts.isEmpty()) {
      // No matching cart
      return null;
    }
    return (Cart) carts.get(0);
  }

  @Override
  public Cart getInitialisedCartByCustomer(Customer customer) throws CustomerNotFoundException {
    Assert.notNull(customer, "customer cannot be null");

    Cart cart = customer.getCart();
    if (cart == null) {
      // Create a suitable cart
      cart = new Cart();
      customer.setCart(cart);
    }

    // Ensure we associate the Customer and Cart with the current session and initialise the collection
    hibernateTemplate.saveOrUpdate(customer);
    hibernateTemplate.initialize(customer.getCart().getCartItems());

    return cart;

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
