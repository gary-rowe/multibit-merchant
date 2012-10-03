package org.multibit.mbm.db.dto;

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
  TITLE("title", "titles",false),
  /**
   * The author (for written material)
   */
  AUTHOR("author", "authors",false),
  /**
   * The artist (for performed or produced material)
   */
  ARTIST("artist", "artist",false),
  /**
   * The composer (for audio material)
   */
  COMPOSER("composer", "composers",false),
  /**
   * The genre (for classification)
   */
  GENRE("genre", "genres",false),
  /**
   * Appears in search engine results under the title (single sentence with no buzzwords)
   */
  META_SUMMARY("meta_summary", "meta_summaries",false),
  /**
   * Short summary of the item that appears in local search results (short paragraph)
   */
  SUMMARY("summary", "summaries",false),
  /**
   * URI to a thumbnail image associated with the item (typically appears in search results)
   */
  IMAGE_THUMBNAIL_URI("image_thumbnail_uri", "image_thumbnail_uris",true),
  /**
   * URI to a video associated with the item
   */
  VIDEO_URI("video_uri", "video_uris",true),
  /**
   * URI to audio associated with the item
   */
  AUDIO_URI("audio_uri", "audio_uris",true),
  /**
   * Describes the main features of the item (max 50 words)
   */
  FEATURES("feature", "features",false),
  /**
   * Describes the benefits that the user will get from purchasing the item (max 100 words)
   */
  BENEFITS("benefit", "benefits",false),
  /**
   * Describes the experience that the user will get as a result of purchasing the item (max 150 words)
   */
  EXPERIENCE("experience", "experiences",false),
  /**
   * Detailed technical specifications (put all relevant technical information)
   */
  SPECIFICATION("specification", "specifications",false),
  /**
   * An entertaining story associated with the item
   */
  BACK_STORY("back_story", "back_stories",false),
  /**
   * Detailed instructions for use
   */
  INSTRUCTIONS("instruction", "instructions",false),
  /**
   * Dimensions of item associated with length, breadth and width
   */
  SIZE("size", "sizes",false),
  /**
   * Dimensions of item associated with weight (useful for shipping cost calculations)
   */
  WEIGHT("weight", "weights",false),
  /**
   * Dimensions of item associated with weight (useful for shipping cost calculations)
   */
  VOLUME("volume", "volumes",false),
  /**
   * Dimensions of item associated with time (length of video material)
   */
  DURATION("duration", "durations",false),

  // End of enum
  ;
  
  private final String propertyNameSingular;
  private final String propertyNamePlural;
  private final boolean link;

  /**
   *
   * @param propertyNameSingular The singular form
   * @param propertyNamePlural The plural form
   * @param link True if the contents should be expressed as a link
   */
  private ItemField(String propertyNameSingular, String propertyNamePlural, boolean link) {
    this.propertyNamePlural = propertyNamePlural;
    this.propertyNameSingular = propertyNameSingular;
    this.link = link;
  }

  public String getPropertyNamePlural() {
    return propertyNamePlural;
  }

  public String getPropertyNameSingular() {
    return propertyNameSingular;
  }

  public boolean isLink() {
    return link;
  }

  /**
   * @param singular The singular form for the resource name
   * @return The matching ItemField, or null
   */
  public ItemField findBySingular(String singular) {
    for (ItemField itemField: ItemField.values()) {
      if (itemField.getPropertyNameSingular().equals(singular)) {
        return itemField;
      }
    }
    return null;
  }

  /**
   * @param plural The plural form for the resource name
   * @return The matching ItemField, or null
   */
  public ItemField findByPlural(String plural) {
    for (ItemField itemField: ItemField.values()) {
      if (itemField.getPropertyNamePlural().equals(plural)) {
        return itemField;
      }
    }
    return null;
  }
  
}
