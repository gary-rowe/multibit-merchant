package org.multibit.mbm.domain.model.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
 * <p>A Delivery provides the mechanism for temporary storage of {@link Item}s. A {@link Supplier} maintains a single Delivery
 * (created on demand) </p>
 */
@Entity
@Table(name = "deliveries")
public class Delivery implements Serializable {

  private static final long serialVersionUID = 38947590321234L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * Many Deliveries come from a single Supplier
   */
  @ManyToOne()
  private Supplier supplier = null;

  /**
   * A Delivery has many DeliveryItems that need to be eager to have meaning
   * There is no inherent order for this collection
   */
  @OneToMany(targetEntity = DeliveryItem.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.delivery",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  private Set<DeliveryItem> deliveryItems = Sets.newLinkedHashSet();

  /*
  * Default constructor required for Hibernate
  */
  public Delivery() {
  }

  /*
   * Mandatory field constructor required for builders
   */
  public Delivery(Supplier supplier) {
    Preconditions.checkNotNull(supplier, "supplier cannot be null");
    this.supplier = supplier;
    supplier.getDeliveries().add(this);
  }

  /**
   * Handles the process of updating the Item quantities
   *
   * @param item     The Item (usually source from ItemDao)
   * @param quantity The quantity (>0 is add/update, otherwise remove)
   */
  @Transient
  public void setItemQuantity(Item item, int quantity) {
    Preconditions.checkNotNull(item, "item cannot be null");

    Optional<DeliveryItem> deliveryItemOptional = getDeliveryItemByItem(item);

    if (quantity > 0) {
      if (deliveryItemOptional.isPresent()) {
        // Update
        deliveryItemOptional.get().setQuantity(quantity);
      } else {
        // Insert
        DeliveryItem newDeliveryItem = new DeliveryItem(this, item);
        newDeliveryItem.setQuantity(quantity);
        deliveryItems.add(newDeliveryItem);
      }
    } else {
      if (deliveryItemOptional.isPresent()) {
        // Remove
        deliveryItems.remove(deliveryItemOptional.get());
      }
    }

  }

  /**
   * @param item The Item to search for
   *
   * @return The DeliveryItem that contains the Item, or absent
   */
  @Transient
  public Optional<DeliveryItem> getDeliveryItemByItem(Item item) {
    Preconditions.checkNotNull(item, "item cannot be null");

    for (DeliveryItem deliveryItem : deliveryItems) {
      if (deliveryItem.getItem().equals(item)) {
        return Optional.of(deliveryItem);
      }
    }
    return Optional.absent();
  }

  /**
   * @return The number of separate items
   */
  @Transient
  public int getItemTotal() {
    return deliveryItems.size();
  }

  /**
   *
   * @return The total quantity of all items
   */
  @Transient
  public int getQuantityTotal() {
    int itemCount = 0;
    for (DeliveryItem deliveryItem : deliveryItems) {
      itemCount += deliveryItem.getQuantity();
    }
    return itemCount;
  }

  /**
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The Supplier that owns this Delivery
   */
  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  /**
   * @return The DeliveryItems (contains the quantity, batch reference etc)
   */
  public Set<DeliveryItem> getDeliveryItems() {
    return deliveryItems;
  }

  public void setDeliveryItems(Set<DeliveryItem> deliveryItems) {
    this.deliveryItems = deliveryItems;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Delivery other = (Delivery) obj;

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
    return String.format("Delivery[id=%s]]", id);
  }
}
