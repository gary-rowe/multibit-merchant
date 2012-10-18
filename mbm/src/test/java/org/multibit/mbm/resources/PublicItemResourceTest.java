package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicItemResourceTest extends BaseJerseyResourceTest {
  private final ItemDao itemDao=mock(ItemDao.class);

  private final PublicItemResource testObject=new PublicItemResource();

  @Override
  protected void setUpResources() {

    // Create a throwaway authenticator since this is public access
    setUpAuthenticator();

    // Create the customer Items
    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);

    // Create pages
    List<Item> itemsPage1 = Lists.newArrayList();
    itemsPage1.add(book1);
    List<Item> itemsPage2 = Lists.newArrayList();
    itemsPage2.add(book2);

    // Configure the mock DAO
    // Retrieve
    when(itemDao.getAllByPage(1, 0)).thenReturn(itemsPage1);
    when(itemDao.getAllByPage(1, 1)).thenReturn(itemsPage2);
    when(itemDao.getBySKU("0575088893")).thenReturn(Optional.of(book2));

    testObject.setItemDao(itemDao);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void publicRetrievePromotionalItemsAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/items/promotion")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-public-retrieve-items-page-1.json");

    actualResponse = configureAsClient("/items/promotion")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-public-retrieve-items-page-2.json");

  }

  @Test
  public void publicRetrieveItemBySkuAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/items/0575088893")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-customer-retrieve-item.json");

  }

}
