package org.multibit.mbm.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * <p>Utility to provide the following to all layers:</p>
 * <ul>
 * <li>Provision of standard Joda time formatters and parsers</li>
 * </ul>
 * <p>All times use the UTC time zone unless otherwise specified</p>
 * @since 1.0.0
 *         
 */
public class DateUtils {

  /**
   * @return The current instant in UTC
   */
  public static DateTime nowUtc() {
    return new DateTime().withZone(DateTimeZone.UTC);
  }
}
