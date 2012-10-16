package org.multibit.mbm.client.handlers;

import com.theoryinpractise.halbuilder.ResourceFactory;
import com.yammer.dropwizard.client.JerseyClient;

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

  protected final JerseyClient jerseyClient;
  protected final Locale locale;

  public BaseHandler(Locale locale, JerseyClient jerseyClient) {
    this.locale = locale;
    this.jerseyClient = jerseyClient;
  }

  /**
   * @return A {@link com.theoryinpractise.halbuilder.ResourceFactory} configured for production use
   */
  protected ResourceFactory getResourceFactory() {
    ResourceFactory resourceFactory = new ResourceFactory("http://localhost:8080");
    // TODO Consider re-instating this
//    // Override the default configuration
//    resourceFactory.withRenderer(HalMediaType.APPLICATION_HAL_JSON,
//      MinifiedJsonRenderer.class);
//    resourceFactory.withRenderer(HalMediaType.APPLICATION_HAL_XML,
//      MinifiedXmlRenderer.class);
    return resourceFactory;
  }



}
