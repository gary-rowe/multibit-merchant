package org.multibit.mbm.web.rest.v1;

import com.google.common.collect.Lists;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.service.CatalogService;
import org.multibit.mbm.web.rest.v1.search.SearchResults;
import org.multibit.mbm.web.rest.v1.search.catalog.ItemSearchSummary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    List<Item> items=Lists.newArrayList();
    if (query ==null) {
      // Broad search of front page items
      items = catalogService.getAllItems();
    } else {
      // Assume reference search at present
      Item item = catalogService.getItemFromReference(query);
      items.add(item);
    }

    List<ItemSearchSummary> itemSummaries = Lists.newArrayList();
    for (Item item: items) {
      ItemSearchSummary itemSummary = new ItemSearchSummary(item);
      itemSummaries.add(itemSummary);
    }

    SearchResults<ItemSearchSummary> results = new SearchResults<ItemSearchSummary>(itemSummaries, 1, true);

    return results;
  }
}

