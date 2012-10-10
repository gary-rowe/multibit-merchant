package org.multibit.mbm.api;

import javax.ws.rs.core.MediaType;

/**
 * <p>Additional media types to provide the following to resource endpoints:</p>
 * <ul>
 * <li>Provision of HAL identifier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class ImageMediaType extends MediaType {

  /**
   * "image/png"
   */
  public final static String IMAGE_PNG = "image/png";
  /**
   * "image/png"
   */
  public final static MediaType IMAGE_PNG_TYPE = new MediaType("image", "png");

  /**
   * "image/jpeg"
   */
  public final static String IMAGE_JPEG = "image/jpeg";
  /**
   * "image/jpeg"
   */
  public final static MediaType IMAGE_JPEG_TYPE = new MediaType("image", "jpeg");

}

