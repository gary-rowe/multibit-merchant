package org.multibit.mbm.db.dto;

import com.google.common.collect.Sets;
import org.multibit.mbm.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>DTO to provide the following to the application</p>
 * <ul>
 * <li>Provision of persistent state</li>
 * </ul>
 * <p>A Supplier describes the minimum information required to describe an entity capable of providing stock</p>
 */
@Entity
@Table(name = "suppliers")
public class Supplier implements Serializable {

  private static final long serialVersionUID = 32324950734295L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * A Supplier has many Deliveries
   */
  @OneToOne(mappedBy = "supplier")
  private User user = null;

  @OneToMany(
    targetEntity = Delivery.class,
    cascade = {CascadeType.ALL},
    mappedBy = "supplier",
    orphanRemoval = true
  )
  private Set<Delivery> deliveries = Sets.newLinkedHashSet();

  /*
  * Default constructor required for Hibernate
  */
  public Supplier() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Delivery> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(Set<Delivery> deliveries) {
    this.deliveries = deliveries;
  }

  /**
   * @return The User associated with this Customer
   */
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Supplier other = (Supplier) obj;

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
    return String.format("Supplier[id=%s]]", id);
  }

}
