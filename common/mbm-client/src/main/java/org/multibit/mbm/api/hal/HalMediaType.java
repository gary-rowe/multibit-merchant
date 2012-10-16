package org.multibit.mbm.api.hal;

import javax.ws.rs.core.MediaType;

/**
 * <p>Additional media types to provide the following to resource endpoints:</p>
 * <ul>
 * <li>Provision of HAL identifier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class HalMediaType extends MediaType {

  /**
   * "application/hal+xml"
   */
  public final static String APPLICATION_HAL_XML = "application/hal+xml";
  /**
   * "application/hal+xml"
   */
  public final static MediaType APPLICATION_HAL_XML_TYPE = new MediaType("application", "hal+xml");

  /**
   * "application/hal+json"
   */
  public final static String APPLICATION_HAL_JSON = "application/hal+json";
  /**
   * "application/hal+json"
   */
  public final static MediaType APPLICATION_HAL_JSON_TYPE = new MediaType("application", "hal+json");

}
