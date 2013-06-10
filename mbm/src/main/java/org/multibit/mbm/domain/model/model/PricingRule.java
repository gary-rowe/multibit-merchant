package org.multibit.mbm.domain.model.model;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.joda.money.BigMoney;
import org.multibit.mbm.domain.common.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for an ItemPricingRule (link between Item and PricingRule)</li>
 * </ul>
 * <p>The ItemPricingRule allows a price to be calculated for an Item that is subject
 * to various rules such as:</p>
 * <ul>
 * <li>Basic percentage increase</li>
 * <li>Range-based filtering logic (uses discriminator name to describe implementation)</li>
 * </ul>
 * <p>Each pricing rule has a discriminator column ("rule") that indicates which subclass
 * should be used. This allows a single polymorphic collection to be built up which can then
 * be applied using assigned field values.</p>
 */
@Entity
@Table(name = "pricing_rules")
@DiscriminatorValue("D")
@DiscriminatorColumn(
  name = "rule",
  discriminatorType = DiscriminatorType.STRING
)
public class PricingRule implements Identifiable, Serializable {

  private static final long serialVersionUID = 38947435343837482L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id = null;

  @Transient
  private Optional<Supplier> supplier = Optional.absent();

  @Transient
  private Optional<Customer> customer = Optional.absent();

  @Transient
  private int quantity = 0;

  @OneToMany(
    targetEntity = ItemPricingRule.class,
    cascade = {CascadeType.ALL},
    mappedBy = "primaryKey.pricingRule",
    fetch = FetchType.EAGER,
    orphanRemoval = true
  )
  @OrderBy(value = "index")
  private List<ItemPricingRule> itemPricingRules = Lists.newArrayList();

  /**
   * Default constructor required by Hibernate
   */
  public PricingRule() {
  }

  /**
   * @return True if this rule halts further processing
   */
  @Transient
  public boolean halt() {
    return false;
  }

  /**
   * @return True if this rule should be skipped
   */
  @Transient
  public boolean skip() {
    return false;
  }

  /**
   * Default implementation is to do nothing
   *
   * @param unitPrice The original unit price
   *
   * @return The modified unit price (subject to implementation)
   */
  @Transient
  public BigMoney applyTo(BigMoney unitPrice) {
    return unitPrice;
  }

  /**
   * Required
   *
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The item pricing rules
   */
  public List<ItemPricingRule> getItemPricingRules() {
    return itemPricingRules;
  }

  public void setItemPricingRules(List<ItemPricingRule> itemPricingRules) {
    this.itemPricingRules = itemPricingRules;
  }

  /**
   * @return The Supplier
   */
  @Transient
  public Optional<Supplier> getSupplier() {
    return supplier;
  }

  public void setSupplier(Optional<Supplier> supplier) {
    this.supplier = supplier;
  }

  /**
   * @return The Customer
   */
  @Transient
  public Optional<Customer> getCustomer() {
    return customer;
  }

  public void setCustomer(Optional<Customer> customer) {
    this.customer = customer;
  }

  /**
   * @return The quantity (measured in the same units as the unit price)
   */
  @Transient
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

}
