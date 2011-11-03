package org.multibit.mbm.domain;

import org.multibit.mbm.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>A Customer represents a member of the general public</p>
 */
@Entity
@Table(name="customers")
public class Customer implements Serializable {

  private static final long serialVersionUID = 38947590324750L;

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", unique = true, nullable = false)
  private Long id=null;

  @Column(name="openId", nullable = false)
  private String openId=null;

  @Column(name="email",nullable = true)
  private String emailAddress = null;

  /*
   * Default constructor required for Hibernate
   */
  public Customer() {}

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

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
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

    return ObjectUtils.isEqual(id, other.id,
      openId, other.openId, emailAddress, other.emailAddress);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id, openId, emailAddress);
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%s, openId='%s', emailAddress='%s']]", id, openId, emailAddress);
  }
}
