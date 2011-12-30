package org.multibit.mbm.customer.dto;

import com.google.common.collect.Maps;
import org.multibit.mbm.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

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

  @Column(name = "openId", nullable = false)
  private String openId = null;

  @OneToMany(cascade = CascadeType.ALL)
  @MapKeyEnumerated()
  private Map<ContactMethod, ContactMethodDetail> contactMethodMap = Maps.newLinkedHashMap();

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

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  /**
   * @return The {@link ContactMethod} map
   */
  public Map<ContactMethod, ContactMethodDetail> getContactMethodMap() {
    return contactMethodMap;
  }

  public void setContactMethodMap(Map<ContactMethod, ContactMethodDetail> contactMethodMap) {
    this.contactMethodMap = contactMethodMap;
  }

  /**
   * @param contactMethod The contact method (e.g. "EMAIL", "VOIP")
   * @return The {@link ContactMethodDetail} providing the information, or null if none available
   */
  @Transient
  public ContactMethodDetail getContactMethodDetail(ContactMethod contactMethod) {
    return contactMethodMap.get(contactMethod);
  }

  /**
   * @param contactMethod The contact method (e.g. "EMAIL", "VOIP")
   * @param contactMethodDetail The contact method details providing the email address, or VOIP address etc
   */
  @Transient
  public void setContactMethodDetail(ContactMethod contactMethod, ContactMethodDetail contactMethodDetail) {
    contactMethodMap.put(contactMethod, contactMethodDetail);
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
      id, other.id,
      openId, other.openId
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id, openId);
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%s, openId='%s']]", id, openId);
  }

}
