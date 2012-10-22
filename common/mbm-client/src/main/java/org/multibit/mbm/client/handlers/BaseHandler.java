package org.multibit.mbm.client.handlers;

import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import org.multibit.mbm.client.HalHmacResourceFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

/**
 * <p>Abstract base class to provide the following to representation handlers:</p>
 * <ul>
 * <li>Access to common methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseHandler {

  protected final Locale locale;

  /**
   * @param locale The locale providing i18n information
   */
  public BaseHandler(Locale locale) {
    this.locale = locale;
  }

  /**
   * @return A {@link com.theoryinpractise.halbuilder.ResourceFactory} configured for production use
   */
  protected ResourceFactory getResourceFactory() {
    return new ResourceFactory(HalHmacResourceFactory.INSTANCE.getBaseUri());
  }

  protected ReadableResource readHalRepresentation(String hal) {
    ResourceFactory rf = getResourceFactory();
    Reader reader = new StringReader(hal);
    return rf.readResource(reader);
  }

}
