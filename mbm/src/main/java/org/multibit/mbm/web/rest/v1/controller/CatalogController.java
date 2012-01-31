package org.multibit.mbm.web.rest.v1.controller;

import org.multibit.mbm.catalog.builder.ItemBuilder;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.web.rest.v1.client.BaseResponse;
import org.multibit.mbm.web.rest.v1.client.catalog.CreateItemRequest;
import org.multibit.mbm.web.rest.v1.client.catalog.ItemPagedQueryResponse;
import org.multibit.mbm.web.rest.v1.client.catalog.ItemSearchResponse;
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
      exampleItem = ItemBuilder.getInstance()
        .build();
    } else {
      exampleItem = ItemBuilder.getInstance()
        .setGTIN(query)
        .setSKU(query)
        .addPrimaryFieldDetail(ItemField.TITLE,query,"en")
        .addPrimaryFieldDetail(ItemField.SUMMARY,query,"en")
        .build();
    }

    List<Item> items = catalogService.getPagedItems(new ItemPagedQueryResponse(firstResult, maxResults, exampleItem));

    ItemSearchResponse results = new ItemSearchResponse(firstResult,maxResults,items);

    return results;
  }

  /**
   * Retrieve a single Item
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
    Item item = catalogService.getById(id);

    return new ItemSearchResponse(1,1, item);

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
  public BaseResponse addItem(@RequestBody CreateItemRequest createItemRequest) {
    return new BaseResponse();
  }

}

