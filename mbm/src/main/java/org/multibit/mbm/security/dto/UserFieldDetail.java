package org.multibit.mbm.security.dto;

import com.google.common.collect.Sets;
import org.multibit.mbm.i18n.dto.LocalisedText;

import javax.persistence.*;
import java.util.Set;

/**
 * <p>DTO to provide the following to {@link org.multibit.mbm.security.dto.UserField}:<br>
 * <ul>
 * <li>Placeholder for a set of localised text fields</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "user_field_details")
public class UserFieldDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  @Embedded
  private LocalisedText primaryDetail;

  // This mirroring of the map key is essential to make the queries work
  @Column(name = "user_detail", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private UserField userField;

  @ElementCollection
  @CollectionTable(
    name = "user_field_secondary_details",
    joinColumns = @JoinColumn(name = "user_field_id"
    ))
  @Column(name = "user_field")
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
   * @return The {@link org.multibit.mbm.security.dto.UserField} used as the key
   */
  public UserField getUserField() {
    return userField;
  }

  public void setUserField(UserField userField) {
    this.userField = userField;
  }

  /**
   * @return The primary user field localised text (default locale)
   */
  public LocalisedText getPrimaryDetail() {
    return primaryDetail;
  }

  public void setPrimaryDetail(LocalisedText primaryDetail) {
    this.primaryDetail = primaryDetail;
  }

  /**
   * @return The secondary user fields' localised text (additional locales)
   */
  public Set<LocalisedText> getSecondaryDetails() {
    return secondaryDetails;
  }

  public void setSecondaryDetails(Set<LocalisedText> secondaryDetails) {
    this.secondaryDetails = secondaryDetails;
  }
}
