package org.multibit.mbm.util;

import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Utility to provide the following to security framework:</p>
 * <ul>
 * <li>Access to various OpenId parameters</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
public class OpenIdUtils {


  /**
   * @param oid The OpenID authentication token
   *
   * @return The attribute values (never null)
   */
  public static List<String> getAttributeValuesByName(OpenIDAuthenticationToken oid, String name) {

    for (OpenIDAttribute attribute : oid.getAttributes()) {
      if (attribute.getName().equals(name)) {
        return attribute.getValues();
      }
    }

    return new ArrayList<String>();
  }
}
