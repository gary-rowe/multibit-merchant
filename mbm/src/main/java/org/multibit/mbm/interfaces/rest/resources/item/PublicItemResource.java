package org.multibit.mbm.interfaces.rest.resources.item;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.api.Representation;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.common.Representations;
import org.multibit.mbm.interfaces.rest.common.ResourceAsserts;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
@Path("/items")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML, MediaType.APPLICATION_JSON})
public class PublicItemResource extends BaseResource {

  @Resource(name = "hibernateItemDao")
  ItemReadService itemReadService;

  /**
   * Provide a paged response of all items in the system
   *
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all items
   */
  @GET
  @Timed
  @Path("/promotion")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrievePromotionalItemsByPage(
    @QueryParam("ps") Optional<String> rawPageSize,
    @QueryParam("pn") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    PaginatedList<Item> items = itemReadService.getPaginatedList(pageSize, pageNumber);

    // Provide a representation to the client
    Representation representation = Representations.asPaginatedList(self(), "items", items, "items/{id}");

    return ok(representation);

  }

  /**
   * Provide a paged response of all items in the system
   *
   * @param rawSku The unvalidated Stock Keeping Unit (SKU)
   *
   * @return A response containing a paged list of all items
   */
  @GET
  @Timed
  @Path("/{sku}")
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveBySku(@PathParam("sku") String rawSku) {

    // Validation
    // TODO Work out how to validate a SKU
    String sku = rawSku;

    Optional<Item> itemOptional = itemReadService.getBySKU(sku);
    ResourceAsserts.assertPresent(itemOptional, "item");

    // Provide a representation to the client
    Representation representation = Representations.asDetail(self(), itemOptional.get(), Maps.<String, String>newHashMap());

    return ok(representation);

  }

  public void setItemReadService(ItemReadService itemReadService) {
    this.itemReadService = itemReadService;
  }
}