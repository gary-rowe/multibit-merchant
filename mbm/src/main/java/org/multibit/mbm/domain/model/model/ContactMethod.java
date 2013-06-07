package org.multibit.mbm.domain.model.model;

/**
 *  <p>Enum to provide the following to Customer:<br>
 *  <ul>
 *  <li>Standard keys to locate contact method types</li>
 *  <li>Utility methods to extract particular contact methods</li>
 *  </ul>
 * <p>People can be contacted in a wide variety of different ways and this mechanism helps
 * to reduce the complexity involved in handling the details. This enum will manage the arrangement
 * of the underlying data so that it can be provided to the consuming application in a form that
 * is most appropriate for them to work with. For example, a physical address may consist of many
 * fields </p>
 *  
 */
public enum ContactMethod {

  /**
   * Appears before the name in correspondence, e.g. "Mr", "Lord"
   */
  PREFIX("prefix",false),
  /**
   * Appears after the prefix in correspondence, e.g. "Alice", "Alice Sue"
   */
  NAMES("names",false),
  /**
   * Appears after the names in correspondence, e.g. "Smith"
   */
  LAST_NAME("last_name",false),
  /**
   * Appears after the last name in correspondence, e.g. "PhD"
   */
  SUFFIX("suffix",false),
  /**
   * Appears on the website if user posts something
   */
  NICK_NAME("nick_name",false),
  /**
   * Postal address to use for correspondence (not the same as Invoice/Billing addresses)
   */
  POSTAL_ADDRESS("postal_address",true),
  /**
   * A geographical point to use for correspondence (dead letter drop? physical meeting?)
   */
  LAT_LONG("lat_long",false),
  /**
   * An SMS number
   */
  SMS("sms",true),
  /**
   * A land line, e.g. "+441234 567 890 ext 567"
   */
  LAND_LINE("land_line",true),
  /**
   * A fax line, e.g. "+441234 567 891"
   */
  FAX("fax",true),
  /**
   * A VOIP URI, e.g. "skype:"
   */
  VOIP("voip",true),
  /**
   * An FTP URI, e.g. "ftp:"
   */
  FTP("ftp",true),
  /**
   * An email address
   */
  EMAIL("email",true),
  /**
   * An instant message address
   */
  INSTANT_MESSAGE("instant_message",true),
  /**
   * A LinkedIn(TM) profile URI
   */
  LINKED_IN("linked_in",true),
  /**
   * A Twitter(TM) channel name, e.g. "@Example"
   */
  TWITTER("twitter",true),
  /**
   * A Facebook(TM) profile URI
   */
  FACEBOOK("facebook",true),
  /**
   * A Google+(TM) profile URI
   */
  GOOGLE_PLUS("google_plus",true),

  // End of enum
  ;

  private final String propertyNameSingular;
  private final boolean secondaryDetailSupported;

  ContactMethod(String propertyNameSingular, boolean secondaryDetailSupported) {
    this.propertyNameSingular = propertyNameSingular;
    this.secondaryDetailSupported = secondaryDetailSupported;
  }

  public String getPropertyNameSingular() {
    return propertyNameSingular;
  }

  public boolean isSecondaryDetailSupported() {
    return secondaryDetailSupported;
  }

}
