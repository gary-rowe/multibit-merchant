package org.multibit.mbm.customer.dto;

import org.multibit.mbm.cart.dto.Cart;
import org.multibit.mbm.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>DTO to provide the following to the application</p>
 * <ul>
 * <li>Provision of persistent state</li>
 * </ul>
 * <p>A Customer describes the minimum information required to describe an entity capable of making purchases</p>
 * <p>Note that digital purchases may not have a delivery address associated with them, nor an email address to provide
 * confirmation.</p>
 */
@Entity
@Table(name = "customers")
public class Customer implements Serializable {

  private static final long serialVersionUID = 38947590324750L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;


  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="cart_fk")
  private Cart cart;

  /*
  * Default constructor required for Hibernate
  */
  public Customer() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The shopping Cart associated with this Customer (if present)
   */
  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Customer other = (Customer) obj;

    return ObjectUtils.isEqual(
      id, other.id
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id);
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%s]]", id);
  }

}
