package org.multibit.mbm.web.rest.v1.search.catalog;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.web.rest.v1.search.SearchSummary;

/**
 *  <p>Value object to provide the following to {@link org.multibit.mbm.web.rest.v1.search.SearchResults}:</p>
 *  <ul>
 *  <li>Minimal fields of an Item suitable for use in a search result list</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class ItemSearchSummary implements SearchSummary{
  private final String title;
  private final String summary;
  private final String imgThumbnailUri;

  public ItemSearchSummary(Item item) {
    this.title = item.getItemFieldContent(ItemField.TITLE);
    this.summary = item.getItemFieldContent(ItemField.SUMMARY);
    this.imgThumbnailUri = item.getItemFieldContent(ItemField.IMAGE_THUMBNAIL_URI);
  }

  public String getSummary() {
    return summary;
  }

  public String getTitle() {
    return title;
  }

  public String getImgThumbnailUri() {
    return imgThumbnailUri;
  }
}
