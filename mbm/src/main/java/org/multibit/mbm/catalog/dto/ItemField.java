package org.multibit.mbm.catalog.dto;

/**
 *  <p>Enum to provide the following to {@link Item}:<br>
 *  <ul>
 *  <li>Standard keys to locate descriptive field types</li>
 *  <li>Utility methods to extract particular fields (e.g. summary, benefits etc)</li>
 *  </ul>
 * <p>An item can have many different fields associated with it that need to be localised.
 * Since these fields are essentially lists (summary in English, French, Russian etc) then
 * it is a small step further to make them contained within a map.</p>
 * <p>This approach allows for more fields to be rapidly added to the structure simply by adding an entry here and does
 * not require a schema alteration.</p>
 *  
 */
public enum ItemField {

  /**
   * Appears in the item title
   */
  TITLE("title", "titles"),
  /**
   * Appears in search engine results under the title (single sentence with no buzzwords)
   */
  META_SUMMARY("meta_summary", "meta_summaries"),
  /**
   * Short summary of the item that appears in local search results (short paragraph)
   */
  SUMMARY("summary", "summaries"),
  /**
   * URI to a thumbnail image associated with the item (typically appears in search results)
   */
  IMAGE_THUMBNAIL_URI("image_thumbnail_uri", "image_thumbnail_uris"),
  /**
   * URI to a video associated with the item
   */
  VIDEO_URI("video_uri", "video_uris"),
  /**
   * URI to audio associated with the item
   */
  AUDIO_URI("audio_uri", "audio_uris"),
  /**
   * Describes the main features of the item (max 50 words)
   */
  FEATURES("feature", "features"),
  /**
   * Describes the benefits that the user will get from purchasing the item (max 100 words)
   */
  BENEFITS("benefit", "benefits"),
  /**
   * Describes the experience that the user will get as a result of purchasing the item (max 150 words)
   */
  EXPERIENCE("experience", "experiences"),
  /**
   * Detailed technical specifications (put all relevant technical information)
   */
  SPECIFICATION("specification", "specifications"),
  /**
   * An entertaining story associated with the item
   */
  BACK_STORY("back_story", "back_stories"),
  /**
   * Detailed instructions for use
   */
  INSTRUCTIONS("instruction", "instructions"),
  /**
   * Dimensions of item associated with length, breadth and width
   */
  SIZE("size", "sizes"),
  /**
   * Dimensions of item associated with weight (useful for shipping cost calculations)
   */
  WEIGHT("weight", "weights"),
  /**
   * Dimensions of item associated with weight (useful for shipping cost calculations)
   */
  VOLUME("volume", "volumes"),
  /**
   * Dimensions of item associated with time (length of video material)
   */
  DURATION("duration", "durations"),

  // End of enum
  ;
  
  private final String singularResource;
  private final String pluralResource;

  private ItemField(String singularResource, String pluralResource) {
    this.pluralResource = pluralResource;
    this.singularResource = singularResource;
  }
}
