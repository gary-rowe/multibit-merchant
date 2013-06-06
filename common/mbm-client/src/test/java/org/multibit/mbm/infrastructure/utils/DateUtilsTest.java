package org.multibit.mbm.infrastructure.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
  @Test
  public void testFriendlyFormatDefaultLocale() {
    DateTimeUtils.setCurrentMillisFixed(new DateTime(2000,1,1,0,0,0,0).getMillis());

    assertEquals("Saturday, January 01", DateUtils.formatFriendlyDate(DateUtils.nowUtc()));
  }

  @Test
  public void testFriendlyFormatFrenchLocale() {
    DateTimeUtils.setCurrentMillisFixed(new DateTime(2000,1,1,0,0,0,0).getMillis());

    assertEquals("samedi, janvier 01", DateUtils.formatFriendlyDate(DateUtils.nowUtc(), Locale.FRANCE));
  }

  @Test
  public void testFriendlyFormatThaiLocale() {
    DateTimeUtils.setCurrentMillisFixed(new DateTime(2000,1,1,0,0,0,0).getMillis());

    assertEquals("วันเสาร์, มกราคม 01", DateUtils.formatFriendlyDate(DateUtils.nowUtc(), new Locale("th","TH","TH")));
  }

  @Test
  public void testISO8601DefaultLocale() {
    DateTime instant = DateUtils.parseISO8601("2000-01-01T12:00:00Z");
    assertEquals("2000-01-01T12:00:00Z",DateUtils.formatISO8601(instant));
  }
}
