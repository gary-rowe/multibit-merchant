package org.multibit.mbm.client.handlers;

import com.theoryinpractise.halbuilder.ResourceFactory;
import com.yammer.dropwizard.client.JerseyClient;

import java.net.URI;
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
  protected final URI mbmBaseUri;

  /**
   *
   * @param locale The locale providing i18n information
   * @param jerseyClient The client for retrieving upstream data
   * @param mbmBaseUri The URI identifying the upstream server
   */
  public BaseHandler(Locale locale, JerseyClient jerseyClient, URI mbmBaseUri) {
    this.locale = locale;
    this.jerseyClient = jerseyClient;
    this.mbmBaseUri=mbmBaseUri;
  }

  /**
   * @return A {@link com.theoryinpractise.halbuilder.ResourceFactory} configured for production use
   */
  protected ResourceFactory getResourceFactory() {
    return new ResourceFactory(mbmBaseUri);
  }

}
