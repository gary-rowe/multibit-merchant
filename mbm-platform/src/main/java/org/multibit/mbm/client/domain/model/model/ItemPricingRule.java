package org.multibit.mbm.client.domain.model.model;

import com.google.common.base.Preconditions;
import org.multibit.mbm.client.common.utils.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * DTO to provide the following to application:<br>
 * <ul>
 * <li>Stores state for a PricingRule Item (link between PricingRule and Item)</li>
 * </ul>
 * The ItemPricingRule describes the Items assigned to a particular PricingRule and any additional properties that are
 * specific to the relationship (for example the quantity of a given Item in the PricingRule).
 * </p>
 */
@Entity
@Table(name = "item_pricing_rules")
@AssociationOverrides({
  @AssociationOverride(
    name = "primaryKey.item",
    joinColumns = @JoinColumn(name = "item_id")),
  @AssociationOverride(
    name = "primaryKey.pricingRule",
    joinColumns = @JoinColumn(name = "pricing_rule_id"))
})
public class ItemPricingRule implements Serializable {

  private static final long serialVersionUID = 389475903837482L;

  private ItemPricingRulePk primaryKey = new ItemPricingRulePk();

  @Column(name = "index", nullable = false)
  private int index = 0;

  /**
   * Default constructor required by Hibernate
   */
  public ItemPricingRule() {
  }

  /**
   * Standard constructor with mandatory fields
   *
   * @param pricingRule required pricingRule
   * @param item     required item
   */
  public ItemPricingRule(PricingRule pricingRule, Item item) {
    Preconditions.checkNotNull(pricingRule, "pricingRule is required");
    Preconditions.checkNotNull(item, "item is required");
    primaryKey.setPricingRule(pricingRule);
    primaryKey.setItem(item);
  }

  /**
   * @return Returns the primary key
   */
  @EmbeddedId
  public ItemPricingRulePk getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(ItemPricingRulePk primaryKey) {
    this.primaryKey = primaryKey;
  }

  /**
   * @return Returns primaryKey.getItem()
   */
  @Transient
  public Item getItem() {
    return primaryKey.getItem();
  }

  /**
   * @return Returns primaryKey.getPricingRule()
   */
  @Transient
  public PricingRule getPricingRule() {
    return primaryKey.getPricingRule();
  }

  /**
   * @return The index position of the Pricing Rule (zero-based)
   */
  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  /**
   * Primary key class to set the PricingRule and the Item as primary key in this many to many
   * relationship.
   */
  @Embeddable
  public static class ItemPricingRulePk implements Serializable {

    private static final long serialVersionUID = 1L;
    private Item item;
    private PricingRule pricingRule;

    /**
     * The associated Item
     *
     * @return returns the item
     */
    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "item_id")
    public Item getItem() {
      return item;
    }

    public void setItem(Item item) {
      this.item = item;
    }

    /**
     * The associated PricingRule
     *
     * @return Returns the PricingRule
     */
    @ManyToOne(targetEntity = PricingRule.class)
    public PricingRule getPricingRule() {
      return pricingRule;
    }

    public void setPricingRule(PricingRule pricingRule) {
      this.pricingRule = pricingRule;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ItemPricingRulePk other = (ItemPricingRulePk) obj;

      return ObjectUtils.isEqual(
        pricingRule, other.pricingRule,
        item, other.item
      );
    }

    @Override
    public int hashCode() {
      return ObjectUtils.getHashCode(pricingRule, item);
    }

    @Override
    public String toString() {
      return String.format("ItemPricingRulePk[pricingRule=%s, item=%s]]", pricingRule, item);
    }


  }
}
