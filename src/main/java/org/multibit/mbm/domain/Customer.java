package org.multibit.mbm.domain;

import org.multibit.mbm.util.ObjectUtils;

import java.io.Serializable;
import java.util.UUID;

public final class Customer implements Serializable {

  private static final long serialVersionUID = 2L;
  private final UUID id;
  private final String openId;
  private final String email;

  public Customer(UUID id, String openId, String email) {
    Validate.isNotNull(id, "Id");
    Validate.isNotNull(openId, "OpenId");
    Validate.isNotNull(email, "Email");

    this.id = id;
    this.openId = openId;
    this.email = email;
  }

  public UUID getId() {
    return id;
  }

  public String getOpenId() {
    return openId;
  }

  public String getEmail() {
    return email;
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
      openId, other.openId,
      email, other.email);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(id, openId, email);
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%s, openId='%s', email='%s']", id, openId, email + "]");
  }
}
