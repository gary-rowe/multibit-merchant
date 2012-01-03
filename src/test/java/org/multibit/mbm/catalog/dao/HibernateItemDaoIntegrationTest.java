package org.multibit.mbm.catalog.dao;

import org.junit.Test;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.dto.ItemFieldDetail;
import org.multibit.mbm.i18n.LocalisedText;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test to verify the Hibernate annotations of the DTOs against a generated schema
 * TODO Add in the ItemBuilder
 */
@ContextConfiguration(locations = {"/spring/test-mbm-hibernate-dao.xml"})
public class HibernateItemDaoIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Resource(name= "hibernateItemDao")
  ItemDao testObject;

  @Test
  public void testPersistAndFindByReference() {

    String sku="abc123";
    Item expected = new Item();
    expected.setSKU(sku);

    // Persist with insert
    int originalItemRows = countRowsInTable("items");
    int originalItemFieldDetailRows = countRowsInTable("item_field_details");
    int originalItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    testObject.persist(expected);

    // Session flush: Expect an insert in items only
    int updatedItemRows = countRowsInTable("items");
    int updatedItemFieldDetailRows = countRowsInTable("contact_method_details");
    int updatedItemFieldDetailSecondaryRows = countRowsInTable("contact_method_secondary_details");
    assertThat("Expected session flush for first insert", updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in contact_method_details", updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows));
    assertThat("Unexpected data in contact_method_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows));

    // Perform an update to the Item that cascades to an insert in ItemField (but not secondary)
    ItemFieldDetail summary = new ItemFieldDetail();
    LocalisedText summary_en = new LocalisedText();
    summary_en.setLocaleKey("en");
    summary_en.setContent("test Summary_en");
    summary.setPrimaryDetail(summary_en);

    expected.setItemFieldDetail(ItemField.SUMMARY, summary);
    expected=testObject.persist(expected);

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
    expected=testObject.persist(expected);

    // No session flush: Expect no change to items, contact_method_details, contact_method_secondary_details
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items", updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in item_field_details", updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Unexpected session flush in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows));

    // Force a flush
    testObject.flush();

    // Query against the "reference"
    Item actual=testObject.getItemBySKU("abc123");

    // Session flush: Expect no change to items, contact_method_details, insert into contact_method_secondary_details
    updatedItemRows = countRowsInTable("items");
    updatedItemFieldDetailRows = countRowsInTable("item_field_details");
    updatedItemFieldDetailSecondaryRows = countRowsInTable("item_field_secondary_details");
    assertThat("Unexpected data in items",updatedItemRows, equalTo(originalItemRows+1));
    assertThat("Unexpected data in item_field_details",updatedItemFieldDetailRows, equalTo(originalItemFieldDetailRows+1));
    assertThat("Expected data in item_field_secondary_details", updatedItemFieldDetailSecondaryRows, equalTo(originalItemFieldDetailSecondaryRows+1));

    assertThat(actual,equalTo(expected));


  }

}
