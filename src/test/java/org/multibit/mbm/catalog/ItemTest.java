package org.multibit.mbm.catalog;

import org.junit.Test;
import org.multibit.mbm.i18n.LocalisedText;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ItemTest {

  /**
   * Verifies that the summary field correctly supports localised text
   */
  @Test
  public void testItemFieldDetailSummary() {

    Item testObject = new Item();

    // Configure supporting objects

    // Create the language variants 
    LocalisedText summary_en = new LocalisedText();
    summary_en.setLocaleKey("en");
    summary_en.setContent("English");

    LocalisedText summary_enGB = new LocalisedText();
    summary_enGB.setLocaleKey("en_GB");
    summary_enGB.setContent("British english");

    LocalisedText summary_frFR = new LocalisedText();
    summary_frFR.setLocaleKey("fr_FR");
    summary_frFR.setContent("French french");

    LocalisedText summary_thTHTH = new LocalisedText();
    summary_thTHTH.setLocaleKey("th_TH_TH");
    summary_thTHTH.setContent("Thai with native script");

    // Bind them to the item field detail
    ItemFieldDetail itemFieldDetail = new ItemFieldDetail();
    itemFieldDetail.setPrimaryDetail(summary_en);
    itemFieldDetail.getSecondaryDetails().add(summary_frFR);
    itemFieldDetail.getSecondaryDetails().add(summary_enGB);
    itemFieldDetail.getSecondaryDetails().add(summary_thTHTH);

    // Bind the text fields to the test object
    testObject.getItemFieldMap().put(ItemField.SUMMARY, itemFieldDetail);

    // Build a suitable locale for Thailand with native script
    Locale thaiLocale = new Locale("th","TH","TH");
    
    // Verify the results
    assertEquals("Primary failed","English", testObject.getItemFieldContent(ItemField.SUMMARY));
    assertEquals("Specific locale (UK) failed","British english", testObject.getItemFieldContent(ItemField.SUMMARY, Locale.UK));
    assertEquals("Specific locale (FRANCE) failed","French french", testObject.getItemFieldContent(ItemField.SUMMARY, Locale.FRANCE));
    assertEquals("Specific locale (THAILAND) failed","Thai with native script", testObject.getItemFieldContent(ItemField.SUMMARY, thaiLocale));

  }
}
