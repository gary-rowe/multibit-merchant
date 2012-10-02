package org.multibit.mbm.db.dto;

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
  LAST_NAME("lastName",false),
  /**
   * Appears after the last name in correspondence, e.g. "PhD"
   */
  SUFFIX("suffix",false),
  /**
   * Postal address to use for correspondence (not the same as Invoice/Billing addresses)
   */
  POSTAL_ADDRESS("postalAddress",true),
  /**
   * A geographical point to use for correspondence (dead letter drop? physical meeting?)
   */
  LAT_LONG("latlong",false),
  /**
   * An SMS number
   */
  SMS("sms",true),
  /**
   * A land line, e.g. "+441234 567 890"
   */
  LAND_LINE("landLine",true),
  /**
   * A fax line, e.g. "+441234 567 891"
   */
  FAX("fax",true),
  /**
   * A voip URI, e.g. "skype:"
   */
  VOIP_URI("voipUri",true),
  /**
   * An FTP URI, e.g. "ftp:"
   */
  FTP_URI("ftpUri",true),
  /**
   * An email address
   */
  EMAIL("email",true),
  /**
   * An instant message address
   */
  IM_URI("imUri",true),
  /**
   * A LinkedIn(TM) profile URI
   */
  LINKED_IN_URI("linkedInUri",true),
  /**
   * A Twitter(TM) channel name, e.g. "@Example"
   */
  TWITTER_CHANNEL("twitterChannel",true),
  /**
   * A Facebook(TM) profile URI
   */
  FACEBOOK_URI("facebookUri",true),
  /**
   * A Google+(TM) profile URI
   */
  GOOGLE_PLUS_URI("googlePlusUri",true),

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
