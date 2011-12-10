package org.multibit.mbm.customer;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Set;

/**
 *  <p>DTO to provide the following to {@link ContactMethod}:<br>
 *  <ul>
 *  <li>Provision of detail fields for the contact method </li>
 *  </ul>
 *  </p>
 *  
 */
@Entity
@Table(name = "contact_method_details")
public class ContactMethodDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id = null;

  @Column(name = "primary_detail")
  private String primaryDetail;

  @ElementCollection
  @CollectionTable(
    name = "contact_method_secondary_details",
    joinColumns = @JoinColumn(name = "contact_method_id"
    ))
  @Column(name = "secondary_detail")
  private Set<String> secondaryDetails = Sets.newLinkedHashSet();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPrimaryDetail() {
    return primaryDetail;
  }

  public void setPrimaryDetail(String primaryDetail) {
    this.primaryDetail = primaryDetail;
  }

  public Set<String> getSecondaryDetails() {
    return secondaryDetails;
  }

  public void setSecondaryDetails(Set<String> secondaryDetails) {
    this.secondaryDetails = secondaryDetails;
  }
}
