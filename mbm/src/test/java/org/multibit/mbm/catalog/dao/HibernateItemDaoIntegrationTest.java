package org.multibit.mbm.catalog.dao;

import org.junit.Test;
import org.multibit.mbm.catalog.builder.ItemBuilder;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.dto.ItemFieldDetail;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.i18n.dto.LocalisedText;
import org.multibit.mbm.test.BaseIntegrationTests;
import org.multibit.mbm.web.rest.v1.catalog.ItemPagedQuery;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 */
@ContextConfiguration(locations = {"/spring/test-mbm-context.xml"})
public class HibernateItemDaoIntegrationTest extends BaseIntegrationTests {

  @Resource(name= "hibernateCustomerDao")
  CustomerDao customerDao;

  @Resource(name= "hibernateItemDao")
  ItemDao testObject;

  /**
   * Simple inserts and updates
   */
  @Test
  public void testPersistAndFindBySKU() {
    
    String sku="abc123";
    String gtin="def456";

    Item expected = ItemBuilder
      .getInstance()
      .setSKU(sku)
      .setGTIN(gtin)
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
    Item actual=testObject.getBySKU("abc123");

    // Session flush: Expect no change to items, contact_method_details, contact_method_secondary_details
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items",updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in item_field_details",updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Unexpected data in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows+1));

    assertThat(actual,equalTo(expected));
    assertThat(actual.getGTIN(),equalTo("def456"));


  }

  /**
   * Verifies that a populated database can be searched and paged
   */
  @Test
  public void testGetPagedItems() {

    testObject.flush();

    // All items (check against inefficient joins)
    ItemPagedQuery itemPagedQuery = new ItemPagedQuery(0,10,null);
    List<Item> items = testObject.getPagedItems(itemPagedQuery);

    // TODO Examine the transaction boundaries for these tests
    assertThat("Unexpected data in Item page 1", items.size(), equalTo(5));
    assertThat("Unexpected data ordering in Item [0,1]", items.get(0).getId(), equalTo(1L));
    assertThat("Unexpected data ordering in Item [1,1]", items.get(1).getId(), equalTo(2L));
    assertThat("Unexpected data ordering in Item [2,1]", items.get(2).getId(), equalTo(3L));
    assertThat("Unexpected data ordering in Item [3,1]", items.get(3).getId(), equalTo(4L));
    assertThat("Unexpected data ordering in Item [4,1]", items.get(4).getId(), equalTo(5L));

    // Page 1
    itemPagedQuery = new ItemPagedQuery(0,2,null);
    items = testObject.getPagedItems(itemPagedQuery);
    
    assertThat("Unexpected data in Item page 1", items.size(), equalTo(2));
    assertThat("Unexpected data ordering in Item [0,1]", items.get(0).getId(), equalTo(1L));
    assertThat("Unexpected data ordering in Item [1,1]", items.get(1).getId(), equalTo(2L));

    // Page 2
    itemPagedQuery = new ItemPagedQuery(2,2,null);
    items = testObject.getPagedItems(itemPagedQuery);

    assertThat("Unexpected data in Item page 2", items.size(), equalTo(2));
    assertThat("Unexpected data ordering in Item [0,2]", items.get(0).getId(), equalTo(3L));
    assertThat("Unexpected data ordering in Item [1,2]", items.get(1).getId(), equalTo(4L));

    // Page 3
    itemPagedQuery = new ItemPagedQuery(4,2,null);
    items = testObject.getPagedItems(itemPagedQuery);

    assertThat("Unexpected data in Item page 3", items.size(), equalTo(1));
    assertThat("Unexpected data ordering in Item [0,3]", items.get(0).getId(), equalTo(5L));

    // Predicated searches

    // Search in the primary TITLE field
    Item example = ItemBuilder
      .getInstance()
      .addPrimaryFieldDetail(ItemField.TITLE, "Central Heating", "en")
      .build();
    itemPagedQuery = new ItemPagedQuery(0,5,example);
    items = testObject.getPagedItems(itemPagedQuery);

    assertThat("Unexpected data in Item page 1 (title)", items.size(), equalTo(1));
    assertThat("Unexpected data ordering in Item (title) [0,1]", items.get(0).getId(), equalTo(3L));

    // Search in both primary TITLE and SUMMARY field (only SUMMARY will succeed)
    example = ItemBuilder
      .getInstance()
      .addPrimaryFieldDetail(ItemField.TITLE, "aardvark", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "trust me", "en")
      .build();
    itemPagedQuery = new ItemPagedQuery(0,5,example);
    items = testObject.getPagedItems(itemPagedQuery);

    assertThat("Unexpected data in Item page 1 (summary)", items.size(), equalTo(1));
    assertThat("Unexpected data ordering in Item (summary) [0,1]", items.get(0).getId(), equalTo(1L));

  }

}
