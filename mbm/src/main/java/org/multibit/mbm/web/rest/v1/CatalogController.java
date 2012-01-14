package org.multibit.mbm.web.rest.v1;

import com.google.common.collect.Lists;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.web.rest.v1.catalog.ItemDetail;
import org.multibit.mbm.web.rest.v1.catalog.ItemSearchSummary;
import org.multibit.mbm.web.rest.v1.search.SearchResults;
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
   * TODO Make this a generic search facility against items
   * Consider support for named parameters (e.g. category:, reference: etc)
   * Consider caching strategies (e.g. DAO against ItemFieldDetail with word index)
   * Consider a Lucene or Hibernate Search implementation
   *
   * @param query The query to decode
   *
   * @return A batch of matching results
   */
  @RequestMapping(value = "/catalog/item/search", method = RequestMethod.GET)
  @ResponseBody
  public SearchResults<ItemSearchSummary> search(@RequestParam(value = "q", required = false) String query) {

    List<Item> items = Lists.newArrayList();
    if (query == null) {
      // Broad search of front page items
      items = catalogService.getAllItems();
    } else {
      // Assume SKU search at present
      Item item = catalogService.getBySKU(query);
      items.add(item);
    }

    List<ItemSearchSummary> itemSummaries = Lists.newArrayList();
    for (Item item : items) {
      ItemSearchSummary itemSummary = new ItemSearchSummary(item);
      itemSummaries.add(itemSummary);
    }

    SearchResults<ItemSearchSummary> results = new SearchResults<ItemSearchSummary>(itemSummaries, 1, true);

    return results;
  }

  // /mbm/api/v1/catalog/item/{id}/{slug}

  /**
   * Retrieve a single Item
   * Consider support for named parameters (e.g. category:, reference: etc)
   * Consider caching strategies (e.g. DAO against ItemFieldDetail with word index)
   * Consider a Lucene or Hibernate Search implementation
   *
   * @param id   The id to locate
   * @param slug The slug (ignored but helps with SEO)
   *
   * @return A batch of matching results
   */
  @RequestMapping(value = "/catalog/item/{id}/{slug}", method = RequestMethod.GET)
  @ResponseBody
  public ItemDetail findById(@PathVariable(value = "id") Long id, @PathVariable(value = "slug") String slug) {

    // Broad search of front page items
    Item item = catalogService.getById(id);

    return new ItemDetail(item);

  }

}

