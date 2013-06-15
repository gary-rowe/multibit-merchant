package org.multibit.mbm.client.domain.model.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemTest {

  /**
   * Verifies that the summary field correctly supports localised text
   */
  @Test
  public void testItemFieldDetailSummary() {

    // Use the builder
    Item testObject= ItemBuilder.newInstance()
      .withSKU("abc123")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "English", "en")
      .withSecondaryFieldDetail(ItemField.SUMMARY, "French french", "fr_FR")
      .withSecondaryFieldDetail(ItemField.SUMMARY, "British english", "en_GB")
      .withSecondaryFieldDetail(ItemField.SUMMARY, "Thai with native script", "th_TH_TH")
      .withPrimaryFieldDetail(ItemField.TITLE, "English title", "en")
      .withSecondaryFieldDetail(ItemField.TITLE, "French title", "fr_FR")
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
