package org.multibit.mbm.client.interfaces.rest.links;

/**
 * <p>Interface to provide the following to resources:</p>
 * <ul>
 * <li>Provision of a single point of reference for application link relations</li>
 * </ul>
 * <h3>A note on relative links</h3>
 * <p>Representations rely on relative links to allow clients to navigate around the application without needing to
 * create internal "magic strings". In HAL, the "_links" section is provided for JSON and XML representations.</p>
 */
public interface ApplicationLinkRelations {

  // Common security relations
  String USER_REL = "user";
  String ROLE_REL = "role";
  String USER_ROLE_REL = "user_role";

  // Application specific relations
  String CUSTOMER_REL = "customer";
  String SUPPLIER_REL = "supplier";

  String CART_REL = "cart";
  String CART_ITEM_REL = "cart_item";

  String DELIVERY_REL = "delivery";
  String DELIVERY_ITEM_REL = "delivery_item";

  String ITEM_REL = "item";

  String PURCHASE_ORDER_REL = "purchase_order";

}
