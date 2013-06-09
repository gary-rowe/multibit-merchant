package org.multibit.mbm.domain.model.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.multibit.mbm.domain.common.Identifiable;
import org.multibit.mbm.infrastructure.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>DTO to provide the following to the application</p>
 * <ul>
 * <li>Provision of persistent state</li>
 * </ul>
 * <p>A PurchaseOrder provides the mechanism for temporary storage of {@link Item}s. A {@link Supplier} maintains a single PurchaseOrder
 * (created on demand) </p>
 */
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder implements Identifiable, Serializable {

  private static final long serialVersionUID = 38947590321234L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  /**
   * Many PurchaseOrders for one Supplier
   */
  @ManyToOne()
  private Supplier supplier = null;

  /**
   * A PurchaseOrder has many PurchaseOrderItems that need to be eager to have meaning
   * There is no inherent order for this collection
   */
  @OneToMany(targetEntity = PurchaseOrderItem.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.purchaseOrder",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  private Set<PurchaseOrderItem> purchaseOrderItems = Sets.newLinkedHashSet();

  /*
  * Default constructor required for Hibernate
  */
  public PurchaseOrder() {
  }

  /*
   * Mandatory field constructor required for builders
   */
  public PurchaseOrder(Supplier supplier) {
    Preconditions.checkNotNull(supplier, "supplier cannot be null");
    this.supplier = supplier;
    supplier.getPurchaseOrders().add(this);
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

    Optional<PurchaseOrderItem> purchaseOrderItemOptional = getPurchaseOrderItemByItem(item);

    if (quantity > 0) {
      if (purchaseOrderItemOptional.isPresent()) {
        // Update
        purchaseOrderItemOptional.get().setQuantity(quantity);
      } else {
        // Insert
        PurchaseOrderItem newPurchaseOrderItem = new PurchaseOrderItem(this, item);
        newPurchaseOrderItem.setQuantity(quantity);
        purchaseOrderItems.add(newPurchaseOrderItem);
      }
    } else {
      if (purchaseOrderItemOptional.isPresent()) {
        // Remove
        purchaseOrderItems.remove(purchaseOrderItemOptional.get());
      }
    }

  }

  /**
   * @param item The Item to search for
   *
   * @return The PurchaseOrderItem that contains the Item, or absent
   */
  @Transient
  public Optional<PurchaseOrderItem> getPurchaseOrderItemByItem(Item item) {
    Preconditions.checkNotNull(item, "item cannot be null");

    for (PurchaseOrderItem purchaseorderItem : purchaseOrderItems) {
      if (purchaseorderItem.getItem().equals(item)) {
        return Optional.of(purchaseorderItem);
      }
    }
    return Optional.absent();
  }

  /**
   * @return The number of separate items
   */
  @Transient
  public int getItemTotal() {
    return purchaseOrderItems.size();
  }

  /**
   *
   * @return The total quantity of all items
   */
  @Transient
  public int getQuantityTotal() {
    int itemCount = 0;
    for (PurchaseOrderItem purchaseorderItem : purchaseOrderItems) {
      itemCount += purchaseorderItem.getQuantity();
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
   * @return The Supplier that will fulfil this PurchaseOrder
   */
  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  /**
   * @return The PurchaseOrderItems (contains the quantity, batch reference etc)
   */
  public Set<PurchaseOrderItem> getPurchaseOrderItems() {
    return purchaseOrderItems;
  }

  public void setPurchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
    this.purchaseOrderItems = purchaseOrderItems;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PurchaseOrder other = (PurchaseOrder) obj;

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
    return String.format("PurchaseOrder[id=%s]]", id);
  }
}
