package org.multibit.mbm.resources.admin;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.item.AdminCreateItemRequest;
import org.multibit.mbm.api.request.item.AdminUpdateItemRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import java.util.List;

import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminItemResourceTest extends BaseJerseyResourceTest {

  private final ItemDao itemDao=mock(ItemDao.class);

  private final AdminItemResource testObject=new AdminItemResource();

  @Override
  protected void setUpResources() {

    // Create the supporting Role
    Role adminRole = DatabaseLoader.buildAdminRole();
    Role customerRole = DatabaseLoader.buildCustomerRole();

    // Create the Item for authenticated access
    User adminUser = setUpAuthenticator(Lists.newArrayList(adminRole));
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
    // Create
    when(itemDao.saveOrUpdate((Item) isNotNull())).thenReturn(book1);
    when(itemDao.getBySKU("sku123")).thenReturn(Optional.<Item>absent());
    // Retrieve
    when(itemDao.getAllByPage(1,1)).thenReturn(itemsPage1);
    when(itemDao.getAllByPage(1,2)).thenReturn(itemsPage2);
    // Update
    when(itemDao.getById(1L)).thenReturn(Optional.of(book1));
    when(itemDao.getById(2L)).thenReturn(Optional.of(book2));
    when(itemDao.getBySKU("0099410672")).thenReturn(Optional.of(book1));

    testObject.setItemDao(itemDao);

    // Configure resources
    addResource(testObject);

  }

  @Test
  public void adminCreateItemAsHalJson() throws Exception {

    AdminCreateItemRequest createItemRequest = new AdminCreateItemRequest();
    createItemRequest.setSKU("sku123");

    String actualResponse = client()
      .resource("/admin/item")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(createItemRequest)
      .post(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("CreateItem by admin response render to HAL+JSON",actualResponse, "fixtures/hal/item/expected-admin-create-item.json");

  }

  @Test
  public void adminRetrieveItemAsHalJson() throws Exception {

    String actualResponse = client()
      .resource("/admin/item")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 1 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/item/expected-admin-retrieve-items-page-1.json");

    actualResponse = client()
      .resource("/admin/item")
      .queryParam("pageSize","1")
      .queryParam("pageNumber", "2")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Item list 2 can be retrieved as HAL+JSON", actualResponse, "fixtures/hal/item/expected-admin-retrieve-items-page-2.json");

  }

  @Test
  public void adminUpdateItemAsHalJson() throws Exception {

    AdminUpdateItemRequest updateItemRequest = new AdminUpdateItemRequest();
    updateItemRequest.setSKU("sku123");
    updateItemRequest.setGTIN("gtin123");

    String actualResponse = client()
      .resource("/admin/item/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateItemRequest)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateItem by admin response render to HAL+JSON",actualResponse, "fixtures/hal/item/expected-admin-update-item.json");

  }

  @Test
  public void adminDeleteItemAsHalJson() throws Exception {

    AdminDeleteEntityRequest deleteItemRequest = new AdminDeleteEntityRequest();
    deleteItemRequest.setReason("No longer available");

    String actualResponse = client()
      .resource("/admin/item/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(deleteItemRequest)
      .delete(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("DeleteItem by admin response render to HAL+JSON",actualResponse, "fixtures/hal/item/expected-admin-delete-item.json");

  }

}
