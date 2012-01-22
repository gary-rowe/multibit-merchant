package org.multibit.mbm.security.dto;

/**
 *  <p>Enum to provide the following to {@link org.multibit.mbm.catalog.dto.Item}:<br>
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
public enum UserField {

  /**
   * Appears in the item title
   */
  VERIFICATION("verification", "verifications"),

  // End of enum
  ;

  private final String singular;
  private final String plural;

  private UserField(String singular, String plural) {
    this.plural = plural;
    this.singular = singular;
  }

  public String getPlural() {
    return plural;
  }

  public String getSingular() {
    return singular;
  }

  /**
   * @param singular The singular form for the resource name
   * @return The matching UserField, or null
   */
  public UserField findBySingular(String singular) {
    for (UserField userField: UserField.values()) {
      if (userField.getSingular().equals(singular)) {
        return userField;
      }
    }
    return null;
  }

  /**
   * @param plural The plural form for the resource name
   * @return The matching UserField, or null
   */
  public UserField findByPlural(String plural) {
    for (UserField userField: UserField.values()) {
      if (userField.getPlural().equals(plural)) {
        return userField;
      }
    }
    return null;
  }

}
