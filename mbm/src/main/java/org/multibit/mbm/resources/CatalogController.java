package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import org.multibit.mbm.api.request.item.AdminCreateItemRequest;
import org.multibit.mbm.api.response.BaseResponse;
import org.multibit.mbm.api.response.ItemPagedQueryResponse;
import org.multibit.mbm.api.response.ItemSearchResponse;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemBuilder;
import org.multibit.mbm.db.dto.ItemField;
import org.multibit.mbm.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Handles the RESTful APIs for the Catalog package
 */
@Controller
@RequestMapping(value = "/v1")
public class CatalogController {

  @Resource(name = "catalogService")
  private CatalogService catalogService = null;

  /**
   * Search service for locating Items
   *
   * @param query       The query to decode (will be applied to common fields)
   * @param firstResult The first result index (zero-based)
   * @param maxResults  The maximum number of results
   *
   * @return A batch of matching results with the given size limit
   */
  @RequestMapping(
    value = "/items",
    method = RequestMethod.GET)
  @ResponseBody
  public ItemSearchResponse search(
    @RequestParam(value = "q", required = false) String query,
    @RequestParam(value = "firstResult", required = false, defaultValue = "0") int firstResult,
    @RequestParam(value = "maxResults", required = false, defaultValue = "25") int maxResults) {

    Item exampleItem;
    if (query == null) {
      // No query so default to the promotional items
      // TODO Implement promotional rules
      exampleItem = ItemBuilder.newInstance()
        .build();
    } else {
      exampleItem = ItemBuilder.newInstance()
        .withGTIN(query)
        .withSKU(query)
        .withPrimaryFieldDetail(ItemField.TITLE, query, "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, query, "en")
        .build();
    }

    List<Item> items = catalogService.getPagedItems(new ItemPagedQueryResponse(firstResult, maxResults, exampleItem));

    ItemSearchResponse results = new ItemSearchResponse(firstResult,maxResults,items);

    return results;
  }

  /**
   * Retrieve a single Item
   * TODO Various listed below
   * Consider support for named parameters (e.g. category:, reference: etc)
   * Consider caching strategies (e.g. DAO against ItemFieldDetail with word index)
   * Consider a Lucene or Hibernate Search implementation
   *
   * @param id   The id to locate
   *
   * @return A batch of matching results
   */
  @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
  @ResponseBody
  public ItemSearchResponse findById(
    @PathVariable(value = "id") Long id
  ) {

    // Broad search of front page items
    Optional<Item> item = catalogService.getById(id);

    return new ItemSearchResponse(1,1, item.get());

  }

  /**
   * Create a single Item
   * @param createItemRequest
   * @return
   */
  @RequestMapping(
    headers = {"content-type=application/json"},
    method = RequestMethod.POST,
    value = "/item"
  )
  @ResponseBody
  public BaseResponse addItem(@RequestBody AdminCreateItemRequest createItemRequest) {
    return new BaseResponse();
  }

}

