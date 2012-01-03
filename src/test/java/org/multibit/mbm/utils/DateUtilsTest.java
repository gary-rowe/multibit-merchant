package org.multibit.mbm.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.multibit.mbm.util.DateUtils;

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

}
