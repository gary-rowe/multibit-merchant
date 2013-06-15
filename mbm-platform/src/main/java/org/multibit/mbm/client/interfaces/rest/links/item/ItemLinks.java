package org.multibit.mbm.client.interfaces.rest.links.item;

import org.multibit.mbm.client.domain.model.model.Item;
import org.multibit.mbm.client.interfaces.rest.links.Links;

import java.net.URI;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to provide links between entities</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ItemLinks extends Links {

  public static final String PUBLIC_SELF_TEMPLATE = "/item";
  public static final String ADMIN_SELF_TEMPLATE = "/items/{id}";

  /**
   * @return A URI suitable for use by the public (normally based on an inferred relationship without an ID)
   */
  public static URI self() {
    return createSelfUri(PUBLIC_SELF_TEMPLATE);
  }

  /**
   * @return A URI suitable for use by an admin (normally based on an ID)
   */
  public static URI adminSelf(Item item) {
    return createSelfUri(ADMIN_SELF_TEMPLATE, item.getId().toString());
  }


}
