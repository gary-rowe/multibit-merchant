package org.multibit.mbm.web.rest.v1;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  <p>[Pattern] to provide the following to [related classes]:<br>
 *  <ul>
 *  <li></li>
 *  </ul>
 *  Example:<br>
 *  <pre>
 *  </pre>
 *  </p>
 *  
 */
@XmlRootElement(name = "time")
@XmlAccessorType(XmlAccessType.FIELD)
public class DateTimeSummary {

  @XmlElement
  private final int year;
  @XmlElement
  private final int month;
  @XmlElement
  private final int day;
  @XmlElement
  private final int hour;
  @XmlElement
  private final int minute;
  @XmlElement
  private final int second;
  @XmlElement
  private final String timeZone;

  public DateTimeSummary(DateTime now) {
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
