package org.multibit.merchant.client.store.resources;

import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage Item retrieval operations
 * by the public</li>
 * </ul>
 * <p>This is the main interaction point for the public to get detail on Items for sale.</p>
 *
 * @since 0.0.1
 */
@Component
@Path("/home")
@Produces(MediaType.TEXT_HTML)
public class RootResource {

  /**
   * Provide the initial view on to the system
   *
   * @return A localised view containing HTML
   */
  @GET
  @Timed
  @CacheControl(maxAge = 5, maxAgeUnit = TimeUnit.MINUTES)
  public RootView retrieveAllByPage() {

    return new RootView("Hello");

  }
}
