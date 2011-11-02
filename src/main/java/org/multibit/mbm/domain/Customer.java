package org.multibit.mbm.domain;

import org.multibit.mbm.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * <p>A Customer represents a member of the general public</p>
 */
@Entity(name = "Customer")
@Table(name="customers")
public class Customer implements Serializable {

  @Id
  @Column(name="id", nullable = false)
  private final UUID id;

  @Column(name="openId", nullable = false)
  private final String openId;

  @Column(name="email",nullable = true)
  private String emailAddress = null;

  public Customer(UUID id, String openId) {
    Validate.isNotNull(id, "Id");
    Validate.isNotNull(openId, "OpenId");

    this.id = id;
    this.openId = openId;
  }

  public UUID getId() {
    return id;
  }

  public String getOpenId() {
    return openId;
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
