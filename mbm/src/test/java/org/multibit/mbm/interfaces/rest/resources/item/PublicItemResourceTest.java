package org.multibit.mbm.interfaces.rest.resources.item;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.domain.common.pagination.PaginatedArrayList;
import org.multibit.mbm.domain.common.pagination.PaginatedLists;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicItemResourceTest extends BaseJerseyHmacResourceTest {
  private final ItemReadService itemReadService =mock(ItemReadService.class);

  private final PublicItemResource testObject=new PublicItemResource();

  @Override
  protected void setUpResources() {

    // Create a throwaway authenticator since this is public access
    setUpClientHmacAuthenticator();

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
    PaginatedArrayList<Item> page1 = PaginatedLists.newPaginatedArrayList(1, 2, 1, itemsPage1);
    PaginatedArrayList<Item> page2 = PaginatedLists.newPaginatedArrayList(2,2, 1, itemsPage2);

    // Retrieve
    when(itemReadService.getPaginatedList(1, 1)).thenReturn(page1);
    when(itemReadService.getPaginatedList(1, 2)).thenReturn(page2);
    when(itemReadService.getBySKU("0575088893")).thenReturn(Optional.of(book2));

    testObject.setItemReadService(itemReadService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void publicRetrievePromotionalItemsAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/items/promotion")
      .queryParam("ps","1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-public-retrieve-items-page-1.json");

    actualResponse = configureAsClient("/items/promotion")
      .queryParam("ps","1")
      .queryParam("pn", "2")
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
