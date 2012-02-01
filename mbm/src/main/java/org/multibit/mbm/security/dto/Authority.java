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

  // Roles (act as EnumSets from the fine grained authorities defined later)
  // Internal roles
  ROLE_ADMIN(true),
  ROLE_SALES(true),
  ROLE_SALES_MANAGER(true),
  ROLE_MARKETING(true),
  ROLE_MARKETING_MANAGER(true),
  ROLE_FINANCE(true),
  ROLE_FINANCE_MANAGER(true),
  ROLE_STORES(true),
  ROLE_STORES_MANAGER(true),
  ROLE_DELIVERY(true),
  ROLE_DELIVERY_MANAGER(true),

  // External roles
  ROLE_CUSTOMER(false),

  // Granted authorities
  // Users
  CHANGE_OWN_PASSWORD(false), // Customers can change their password

  // Invoices (finance)
  CREATE_INVOICES(true),
  RETRIEVE_INVOICES(true),
  UPDATE_INVOICES(true),
  DELETE_INVOICES(true),

  // Orders (sales, stores, finance, delivery)
  CREATE_ORDERS(true),
  RETRIEVE_ORDERS(false), // Customers can view their orders
  UPDATE_ORDERS(true),
  DELETE_ORDERS(true),
  
  // Customers (sales, finance)
  CREATE_CUSTOMERS(true),
  RETRIEVE_CUSTOMERS(true),
  UPDATE_CUSTOMERS(true),
  DELETE_CUSTOMERS(true),

  // Items (sales, stores)
  CREATE_ITEMS(true),
  RETRIEVE_ITEMS(false), // Customers can view items
  UPDATE_ITEMS(true),
  DELETE_ITEMS(true),

  // Prices (sales, finance)
  CREATE_PRICES(true),
  RETRIEVE_PRICES(true),
  UPDATE_PRICES(true),
  DELETE_PRICES(true),

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

