package org.multibit.mbm.persistence.dto;

import org.junit.Assert;
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
      .setSKU("abc123")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "English", "en")
      .addSecondaryFieldDetail(ItemField.SUMMARY, "French french", "fr_FR")
      .addSecondaryFieldDetail(ItemField.SUMMARY, "British english", "en_GB")
      .addSecondaryFieldDetail(ItemField.SUMMARY, "Thai with native script", "th_TH_TH")
      .addPrimaryFieldDetail(ItemField.TITLE, "English title", "en")
      .addSecondaryFieldDetail(ItemField.TITLE, "French title", "fr_FR")
      .build();
   
    // Verify the results
    // SUMMARY
    Assert.assertEquals("Primary failed", "English", testObject.getItemFieldContent(ItemField.SUMMARY));
    Assert.assertEquals("Specific locale (UK) failed", "British english", testObject.getItemFieldContent(ItemField.SUMMARY, "en_GB"));
    Assert.assertEquals("Specific locale (FRANCE) failed", "French french", testObject.getItemFieldContent(ItemField.SUMMARY, "fr_FR"));
    Assert.assertEquals("Specific locale (THAILAND) failed", "Thai with native script", testObject.getItemFieldContent(ItemField.SUMMARY, "th_TH_TH"));

    // TITLE
    Assert.assertEquals("Primary failed", "English title", testObject.getItemFieldContent(ItemField.TITLE));
    Assert.assertEquals("Specific locale (FRANCE) failed", "French title", testObject.getItemFieldContent(ItemField.TITLE, "fr_FR"));

  }
}
