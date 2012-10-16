package org.multibit.mbm.client.item;

import com.yammer.dropwizard.client.JerseyClient;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.model.PublicItem;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Locale;

/**
 * <p>Handler to provide the following to {@link org.multibit.mbm.client.PublicMerchantClient}:</p>
 * <ul>
 * <li>Construction of public single item requests</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class PublicItemHandler {

  private final JerseyClient jerseyClient;
  private final Locale locale;

  /**
   * @param jerseyClient The {@link com.yammer.dropwizard.client.JerseyClient} for consuming the upstream data
   * @param locale       The locale that applies to the request
   */
  public PublicItemHandler(JerseyClient jerseyClient, Locale locale) {
    this.jerseyClient = jerseyClient;
    this.locale = locale;
  }

  /**
   * Retrieve single item using its ID
   *
   * @param id The ID
   * @return A mtching {@link org.multibit.mbm.model.PublicItem}
   */
  public PublicItem retrieveById(long id) {

    // Sanity check
    // TODO Consider Guava ranges?
    if (id < 0) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    // TODO Replace "magic string" with auto-discover based on link rel
    String rawUri = String.format("http://localhost:8080/mbm/items/%d"
      ,id);
    URI uri = URI.create(rawUri);

    PublicItem item = jerseyClient
      .resource(uri)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .header(HttpHeaders.ACCEPT_LANGUAGE, locale.toString())
      .get(PublicItem.class);

    return item;
  }
}
