package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

/**
 * <p>Response to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of a simple date time encoding</li>
 * </ul>
 *
 * @since 0.0.1  
 */
public class DateTimeResponse {

  @JsonProperty
  private final int year;

  @JsonProperty
  private final int month;

  @JsonProperty
  private final int day;

  @JsonProperty
  private final int hour;

  @JsonProperty
  private final int minute;

  @JsonProperty
  private final int second;

  @JsonProperty
  private final String timeZone;

  public DateTimeResponse(DateTime now) {
    this.year = now.getYear();
    this.month = now.getMonthOfYear();
    this.day = now.getDayOfMonth();
    this.hour = now.getHourOfDay();
    this.minute = now.getMinuteOfHour();
    this.second = now.getSecondOfMinute();
    this.timeZone = now.getZone().toString();
  }

  public int getDay() {
    return day;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public int getMonth() {
    return month;
  }

  public int getSecond() {
    return second;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public int getYear() {
    return year;
  }
}
