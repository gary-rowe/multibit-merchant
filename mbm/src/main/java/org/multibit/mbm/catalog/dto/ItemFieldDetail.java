package org.multibit.mbm.catalog.dto;

import com.google.common.collect.Sets;
import org.multibit.mbm.i18n.dto.LocalisedText;

import javax.persistence.*;
import java.util.Set;

/**
 * <p>DTO to provide the following to {@link ItemField}:<br>
 * <ul>
 * <li>Placeholder for a set of localised text fields</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "item_field_details")
public class ItemFieldDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  @Embedded
  private LocalisedText primaryDetail;

  // This mirroring of the map key is essential to make the queries work
  @Column(name = "item_detail", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ItemField itemField;
  
  @ElementCollection
  @CollectionTable(
    name = "item_field_secondary_details",
    joinColumns = @JoinColumn(name = "item_field_id"
    ))
  @Column(name = "item_field")
  private Set<LocalisedText> secondaryDetails = Sets.newLinkedHashSet();

  /**
   * @return The internal ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The {@link ItemField} used as the key
   */
  public ItemField getItemField() {
    return itemField;
  }

  public void setItemField(ItemField itemField) {
    this.itemField = itemField;
  }

  /**
   * @return The primary item field localised text (default locale)
   */
  public LocalisedText getPrimaryDetail() {
    return primaryDetail;
  }

  public void setPrimaryDetail(LocalisedText primaryDetail) {
    this.primaryDetail = primaryDetail;
  }

  /**
   * @return The secondary item fields' localised text (additional locales)
   */
  public Set<LocalisedText> getSecondaryDetails() {
    return secondaryDetails;
  }

  public void setSecondaryDetails(Set<LocalisedText> secondaryDetails) {
    this.secondaryDetails = secondaryDetails;
  }
}
