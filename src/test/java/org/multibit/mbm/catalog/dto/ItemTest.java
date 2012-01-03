package org.multibit.mbm.catalog.dto;

import org.junit.Test;
import org.multibit.mbm.catalog.builder.ItemBuilder;

import static org.junit.Assert.assertEquals;

public class ItemTest {

  /**
   * Verifies that the summary field correctly supports localised text
   */
  @Test
  public void testItemFieldDetailSummary() {

    // Use the builder
    Item testObject=ItemBuilder.getInstance()
      .setSku("abc123")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","English")
      .addSecondaryFieldDetail(ItemField.SUMMARY,"fr_FR","French french")
      .addSecondaryFieldDetail(ItemField.SUMMARY,"en_GB","British english")
      .addSecondaryFieldDetail(ItemField.SUMMARY,"th_TH_TH","Thai with native script")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","English title")
      .addSecondaryFieldDetail(ItemField.TITLE,"fr_FR","French title")
      .build();
   
    // Verify the results
    // SUMMARY
    assertEquals("Primary failed","English", testObject.getItemFieldContent(ItemField.SUMMARY));
    assertEquals("Specific locale (UK) failed","British english", testObject.getItemFieldContent(ItemField.SUMMARY, "en_GB"));
    assertEquals("Specific locale (FRANCE) failed","French french", testObject.getItemFieldContent(ItemField.SUMMARY, "fr_FR"));
    assertEquals("Specific locale (THAILAND) failed","Thai with native script", testObject.getItemFieldContent(ItemField.SUMMARY, "th_TH_TH"));

    // TITLE
    assertEquals("Primary failed","English title", testObject.getItemFieldContent(ItemField.TITLE));
    assertEquals("Specific locale (FRANCE) failed","French title", testObject.getItemFieldContent(ItemField.TITLE, "fr_FR"));

  }
}
