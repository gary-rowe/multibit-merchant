package org.multibit.mbm.security.dto;

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

  PREFIX,
  FIRST_NAME,
  LAST_NAME,
  POSTAL_ADDRESS,
  LAT_LONG,
  SMS,
  LAND_LINE,
  FAX,
  VOIP,
  FTP,
  EMAIL,
  TWITTER,
  FACEBOOK,
  GOOGLE_PLUS,

  // End of enum
  ;


}
