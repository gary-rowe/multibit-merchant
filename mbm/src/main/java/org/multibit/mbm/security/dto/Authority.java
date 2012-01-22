package org.multibit.mbm.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *<p>Enumeration to provide the following to {@link Role}:</p>
 *<ul>
 *<li>Provision of standard authorities grouped by Role</li>
 *</ul>
 *<p>An Authority exists to provide an enum key to be mapped into a Spring Security {@link GrantedAuthority}.</p>
 * @since 1.0.0
 *         
 */
public enum Authority {

  // Naming conventions help navigation and avoid duplication
  // An Authority is named as VERB_SUBJECT_ENTITY
  // Verbs should initially follow CRUD (CREATE, RETRIEVE, UPDATE, DELETE)
  // Subjects are based on outward looking relationships (OWN, OTHERS)
  // Entities are based on primary entities (USER, CUSTOMER, CART, ITEM, INVOICE)

  // Users
  CHANGE_OWN_PASSWORD(false),

  // Invoices
  CREATE_INVOICES(true),
  RETRIEVE_INVOICES(true),
  UPDATE_INVOICES(true),
  DELETE_INVOICES(true),

  // Orders
  CREATE_ORDERS(true),
  RETRIEVE_ORDERS(false),
  UPDATE_ORDERS(true),
  DELETE_ORDERS(true),
  
  // Customers
  CREATE_CUSTOMERS(true),
  RETRIEVE_CUSTOMERS(true),
  UPDATE_CUSTOMERS(true),
  DELETE_CUSTOMERS(true),

  // Items
  CREATE_ITEMS(true),
  RETRIEVE_ITEMS(false),
  UPDATE_ITEMS(true),
  DELETE_ITEMS(true),
  
  
  ; // End of enum

  /**
   * True if the authority can only be applied to an internal User (staff)
   */
  private boolean internal = false;

  /**
   * 
   * @param internal
   */
  Authority(boolean internal) {
    this.internal = internal;
  }

  /**
   * @return A representative GrantedAuthority
   */
  public GrantedAuthority toGrantedAuthority() {
    return new SimpleGrantedAuthority(this.name());
  }

  /**
   * @return True if this Authority can be applied to internal Users only, false means both
   */
  public boolean isInternal() {
    return internal;
  }
}

