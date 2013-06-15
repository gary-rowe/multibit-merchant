package org.multibit.mbm.client.infrastructure.persistence.hibernate;

import com.google.common.base.Optional;
import com.xeiam.xchange.currency.MoneyUtils;
import org.junit.Test;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.*;
import org.multibit.mbm.client.domain.repositories.ItemReadService;
import org.multibit.mbm.testing.BaseIntegrationTests;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateItemReadServiceIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateItemDao")
  ItemReadService testObject;

  /**
   * Simple inserts and updates (includes price persistence check)
   */
  @Test
  public void testPersistAndFindBySKU() {
    
    String sku="abc123";
    String gtin="def456";

    Item expected = ItemBuilder
      .newInstance()
      .withSKU(sku)
      .withGTIN(gtin)
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 1.2345678"))
      .build();

    // Persist with insert
    int originalItemRows = countRowsInTable("items");
    int originalItemFieldDetailRows = countRowsInTable("item_field_details");
    int originalItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect an insert in items only
    int updatedItemRows = countRowsInTable("items");
    int updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    int updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Expected session flush for first insert", updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in contact_method_details", updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows));
    assertThat("Unexpected data in contact_method_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows));

    // Perform an update to the Item that cascades to an insert in ItemField (but not secondary)
    ItemFieldDetail summary = new ItemFieldDetail();
    LocalisedText summary_en = new LocalisedText();
    summary_en.setLocaleKey("en");
    summary_en.setContent("test Summary_en");
    summary.setPrimaryDetail(summary_en);
    summary.setItemField(ItemField.SUMMARY);

    expected.setItemFieldDetail(ItemField.SUMMARY, summary);
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to items, insert into item_field_details
    // Note that itemFieldDetail is now a different instance from the persistent one
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Expected data in item_field_details", updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Unexpected data in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows));

    // Perform an update to the Item that cascades to an insert in secondary ItemField
    // due to an addition to the linked reference
    summary = expected.getItemFieldDetail(ItemField.SUMMARY);
    LocalisedText summary_fr = new LocalisedText();
    summary_fr.setLocaleKey("fr");
    summary_fr.setContent("test Summary_fr");

    summary.getSecondaryDetails().add(summary_fr);
    expected=testObject.saveOrUpdate(expected);
    testObject.flush();

    // Session flush: Expect no change to items, item_field_details, insert into item_field_secondary_details
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in item_field_details", updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Unexpected data in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows+1));

    // Query against the SKU
    Optional<Item> actual=testObject.getBySKU("abc123");

    // Session flush: Expect no change to items, contact_method_details, contact_method_secondary_details
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items",updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in item_field_details",updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Unexpected data in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows+1));

    assertThat(actual.get(),equalTo(expected));
    assertThat(actual.get().getGTIN(),equalTo("def456"));
    assertThat(actual.get().getLocalPrice().getCurrencyUnit().getCurrencyCode(),equalTo("BTC"));
    assertThat(actual.get().getLocalPrice().getAmount().toPlainString(),equalTo("1.234567800000"));

  }

  /**
   * Verifies that a populated database can be searched and paged
   */
  @Test
  public void testGetPaginatedList() {

    // All items (check against inefficient joins)
    final PaginatedList<Item> allItems = testObject.getPaginatedList(5, 1);

    assertThat("Unexpected size in Item page 1", allItems.list().size(), equalTo(5));
    assertThat("Unexpected data ordering in Item [0,1]", allItems.list().get(0).getId(), equalTo(1L));
    assertThat("Unexpected data ordering in Item [1,1]", allItems.list().get(1).getId(), equalTo(2L));
    assertThat("Unexpected data ordering in Item [2,1]", allItems.list().get(2).getId(), equalTo(3L));
    assertThat("Unexpected data ordering in Item [3,1]", allItems.list().get(3).getId(), equalTo(4L));
    assertThat("Unexpected data ordering in Item [4,1]", allItems.list().get(4).getId(), equalTo(5L));

    // Page 1
    final PaginatedList<Item> page1 = testObject.getPaginatedList(2, 1);

    assertThat("Unexpected size in Item page 1", page1.list().size(), equalTo(2));
    assertThat("Unexpected data ordering in Item [0,1]", page1.list().get(0).getId(), equalTo(1L));
    assertThat("Unexpected data ordering in Item [1,1]", page1.list().get(1).getId(), equalTo(2L));

    // Page 2
    final PaginatedList<Item> page2 = testObject.getPaginatedList(2, 2);

    assertThat("Unexpected size in Item page 2", page2.list().size(), equalTo(2));
    assertThat("Unexpected data ordering in Item [0,2]", page2.list().get(0).getId(), equalTo(3L));
    assertThat("Unexpected data ordering in Item [1,2]", page2.list().get(1).getId(), equalTo(4L));

    // Page 3
    final PaginatedList<Item> page3 = testObject.getPaginatedList(2, 3);

    assertThat("Unexpected size in Item page 3", page3.list().size(), equalTo(1));
    assertThat("Unexpected data ordering in Item [0,3]", page3.list().get(0).getId(), equalTo(5L));

    // Predicated searches
  }


  /**
   * Verifies that a populated database can be searched and paged
   */
  @Test
  public void testGetByExampleByPage() {

    // Search in the primary TITLE field
    Item example = ItemBuilder
      .newInstance()
      .withPrimaryFieldDetail(ItemField.TITLE, "Central Heating", "en")
      .build();

    final PaginatedList<Item> byTitle = testObject.getPaginatedListByExample(5,1,example);

    assertThat("Unexpected size in Item page 1 (title)", byTitle.list().size(), equalTo(1));
    assertThat("Unexpected data ordering in Item (title) [0,1]", byTitle.list().get(0).getId(), equalTo(3L));

    // Search in both primary TITLE and SUMMARY field (only SUMMARY will succeed)
    example = ItemBuilder
      .newInstance()
      .withPrimaryFieldDetail(ItemField.TITLE, "aardvark", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "trust me", "en")
      .build();

    final PaginatedList<Item> byTitleAndSummary = testObject.getPaginatedListByExample(5,1,example);

    assertThat("Unexpected size in Item page 1 (summary)", byTitleAndSummary.list().size(), equalTo(1));
    assertThat("Unexpected data ordering in Item (summary) [0,1]", byTitleAndSummary.list().get(0).getId(), equalTo(1L));

  }

}
