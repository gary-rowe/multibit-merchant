package org.multibit.mbm.interfaces.rest.resources.item;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.domain.common.pagination.PaginatedArrayList;
import org.multibit.mbm.domain.common.pagination.PaginatedLists;
import org.multibit.mbm.interfaces.rest.api.AdminDeleteEntityDto;
import org.multibit.mbm.interfaces.rest.api.item.AdminCreateItemDto;
import org.multibit.mbm.interfaces.rest.api.item.AdminUpdateItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.domain.model.model.Item;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminItemResourceTest extends BaseJerseyHmacResourceTest {

  private final ItemReadService itemReadService =mock(ItemReadService.class);

  private final AdminItemResource testObject=new AdminItemResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User adminUser = setUpTrentHmacAuthenticator();
    adminUser.setId(1L);

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
    PaginatedArrayList<Item> page1 = PaginatedLists.newPaginatedArrayList(1, 2, itemsPage1);
    PaginatedArrayList<Item> page2 = PaginatedLists.newPaginatedArrayList(2,2, itemsPage2);
    // Create
    when(itemReadService.saveOrUpdate((Item) isNotNull())).thenReturn(book1);
    when(itemReadService.getBySKU("sku123")).thenReturn(Optional.<Item>absent());
    // Retrieve
    when(itemReadService.getPaginatedList(1, 0)).thenReturn(page1);
    when(itemReadService.getPaginatedList(1, 1)).thenReturn(page2);
    // Update
    when(itemReadService.getById(1L)).thenReturn(Optional.of(book1));
    when(itemReadService.getById(2L)).thenReturn(Optional.of(book2));
    when(itemReadService.getBySKU("0099410672")).thenReturn(Optional.of(book1));

    testObject.setItemReadService(itemReadService);

    // Configure resources
    addSingleton(testObject);

  }

  @Test
  public void adminCreateItemAsHalJson() throws Exception {

    AdminCreateItemDto createItemRequest = new AdminCreateItemDto();
    createItemRequest.setSKU("sku123");

    String actualResponse = configureAsClient(AdminItemResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createItemRequest, MediaType.APPLICATION_JSON_TYPE)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateItem by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/item/expected-admin-create-item.json");

  }

  @Test
  public void adminRetrieveItemAsHalJson() throws Exception {

    String actualResponse = configureAsClient(AdminItemResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-admin-retrieve-items-page-1.json");

    actualResponse = configureAsClient(AdminItemResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/item/expected-admin-retrieve-items-page-2.json");

  }

  @Test
  public void adminUpdateItemAsHalJson() throws Exception {

    AdminUpdateItemDto updateItemRequest = new AdminUpdateItemDto();
    updateItemRequest.setSKU("sku123");
    updateItemRequest.setGTIN("gtin123");

    String actualResponse = configureAsClient("/admin/item/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateItemRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateItem by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/item/expected-admin-update-item.json");

  }

  @Test
  public void adminDeleteItemAsHalJson() throws Exception {

    AdminDeleteEntityDto deleteItemRequest = new AdminDeleteEntityDto();
    deleteItemRequest.setReason("No longer available");

    String actualResponse = configureAsClient("/admin/item/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(deleteItemRequest, MediaType.APPLICATION_JSON_TYPE)
      .delete(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("DeleteItem by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/item/expected-admin-delete-item.json");

  }

}
