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
  TITLE,
  /**
   * Appears in search engine results under the title (single sentence with no buzzwords)
   */
  META_SUMMARY,
  /**
   * Short summary of the item that appears in local search results (short paragraph)
   */
  SUMMARY,
  /**
   * Describes the main features of the item (max 50 words)
   */
  FEATURES,
  /**
   * Describes the benefits that the user will get from purchasing the item (max 100 words)
   */
  BENEFITS,
  /**
   * Describes the experience that the user will get as a result of purchasing the item (max 150 words)
   */
  EXPERIENCE,
  /**
   * Detailed technical specifications (put all relevant technical information)
   */
  SPECIFICATION,
  /**
   * An entertaining story associated with the item
   */
  BACK_STORY,
  /**
   * Detailed instructions for use
   */
  INSTRUCTIONS,
  /**
   * Dimensions of item associated with length, breadth and width
   */
  DIMENSIONS_SIZE,
  /**
   * Dimensions of item associated with weight (useful for shipping cost calculations)
   */
  DIMENSIONS_WEIGHT,
  /**
   * Dimensions of item associated with time (length of video material)
   */
  DIMENSIONS_DURATION,

  // End of enum
  ;


}
