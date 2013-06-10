package org.multibit.mbm.interfaces.rest.auth;

/**
 * <p>Enumeration to provide the following to application:</p>
 * <ul>
 * <li>Provision of standard authorities grouped by Role</li>
 * </ul>
 * <p>An Authority exists to provide an enum key to be mapped into an RestrictedTo annotation.</p>
 *
 * @since 0.0.1
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
  /**
   * The administrator role that can reach administration API functions
   */
  ROLE_ADMIN(true),
  /**
   * The administrator for catalog functions (works on items)
   */
  ROLE_CATALOG_ADMIN(true),
  /**
   * Reserved for client applications when making upstream server calls
   */
  ROLE_CLIENT(true),
  /**
   * A member of the sales team
   */
  ROLE_SALES(true),
  /**
   * The manager of the sales team
   */
  ROLE_SALES_MANAGER(true),
  /**
   * A member of the buying team
   */
  ROLE_BUYER(true),
  /**
   * The manager of the buying team
   */
  ROLE_BUYER_MANAGER(true),
  /**
   * A member of the marketing team
   */
  ROLE_MARKETING(true),
  /**
   * The manager of the marketing team
   */
  ROLE_MARKETING_MANAGER(true),
  /**
   * A member of the finance team
   */
  ROLE_FINANCE(true),
  /**
   * The manager of the finance team
   */
  ROLE_FINANCE_MANAGER(true),
  /**
   * A member of the stores team
   */
  ROLE_STORES(true),
  /**
   * The manager of the stores team
   */
  ROLE_STORES_MANAGER(true),
  /**
   * A member of the delivery team
   */
  ROLE_DELIVERY(true),
  /**
   * The manager of the delivery team
   */
  ROLE_DELIVERY_MANAGER(true),
  /**
   * An external supplier
   */
  ROLE_SUPPLIER(true),

  // External roles
  /**
   * An anonymous (public) customer
   */
  ROLE_PUBLIC(false),
  /**
   * An un-authenticated customer in possession of a "remember me" token
   */
  ROLE_PARTIAL(false),
  /**
  * An authenticated customer
  */
  ROLE_CUSTOMER(false),

  // Granted authorities
  // Users
  CHANGE_OWN_PASSWORD(false), // Customers can change their password

  // User administration
  CREATE_USERS(true),
  RETRIEVE_USERS(true),
  UPDATE_USERS(true),
  DELETE_USERS(true),

  // Role administration
  CREATE_ROLES(true),
  RETRIEVE_ROLES(true),
  UPDATE_ROLES(true),
  DELETE_ROLES(true),

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
  DELETE_PRICES(true),; // End of enum

  /**
   * True if the authority can only be applied to an internal User (staff)
   */
  private boolean internal = false;

  /**
   * @param internal True if the authority is appropriate for staff only
   */
  Authority(boolean internal) {
    this.internal = internal;
  }

  /**
   * @return True if this Authority can be applied to internal Users only, false means both
   */
  public boolean isInternal() {
    return internal;
  }

}

