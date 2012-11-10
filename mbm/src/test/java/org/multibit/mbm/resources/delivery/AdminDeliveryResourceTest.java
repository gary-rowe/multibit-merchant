package org.multibit.mbm.resources.delivery;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.delivery.AdminUpdateDeliveryRequest;
import org.multibit.mbm.api.request.delivery.SupplierDeliveryItem;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Delivery;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.delivery.AdminDeliveryResource;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminDeliveryResourceTest extends BaseJerseyHmacResourceTest {

  private final DeliveryDao deliveryDao=mock(DeliveryDao.class);
  private final ItemDao itemDao=mock(ItemDao.class);

  private final AdminDeliveryResource testObject=new AdminDeliveryResource();

  @Override
  protected void setUpResources() {

    // Create the User for authenticated access
    User adminUser = setUpTrentHmacAuthenticator();
    adminUser.setId(1L);

    // Create the supporting Role
    Role supplierRole = DatabaseLoader.buildSupplierRole();

    // Create some Suppliers
    User steveUser = DatabaseLoader.buildSteveSupplier(supplierRole);
    steveUser.setId(1L);
    steveUser.getSupplier().setId(1L);
    steveUser.getSupplier().getDeliveries().iterator().next().setId(1L);

    User samUser = DatabaseLoader.buildSamSupplier(supplierRole);
    samUser.setId(1L);
    samUser.getSupplier().setId(1L);
    samUser.getSupplier().getDeliveries().iterator().next().setId(1L);

    // Configure Steve's Delivery with Items
    Delivery steveDelivery = steveUser.getSupplier().getDeliveries().iterator().next();

    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);

    steveDelivery.setItemQuantity(book1, 1);
    steveDelivery.setItemQuantity(book2, 2);

    // Configure Bob's Delivery with Items
    Delivery samDelivery = samUser.getSupplier().getDeliveries().iterator().next();

    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    samDelivery.setItemQuantity(book3, 3);
    samDelivery.setItemQuantity(book4, 4);

    // Create some mock results
    List<Delivery> deliveryList1 = Lists.newArrayList(steveDelivery);
    List<Delivery> deliveryList2 = Lists.newArrayList(samDelivery);

    // Configure Delivery DAO
    when(deliveryDao.getById(steveDelivery.getId())).thenReturn(Optional.of(steveDelivery));
    when(deliveryDao.getById(samDelivery.getId())).thenReturn(Optional.of(samDelivery));
    when(deliveryDao.getAllByPage(1, 0)).thenReturn(Lists.newLinkedList(deliveryList1));
    when(deliveryDao.getAllByPage(1, 1)).thenReturn(Lists.newLinkedList(deliveryList2));
    when(deliveryDao.saveOrUpdate(steveDelivery)).thenReturn(steveDelivery);
    when(deliveryDao.saveOrUpdate(samDelivery)).thenReturn(samDelivery);

    // Configure Item DAO
    when(itemDao.getBySKU(book1.getSKU())).thenReturn(Optional.of(book1));
    when(itemDao.getBySKU(book2.getSKU())).thenReturn(Optional.of(book2));
    when(itemDao.getBySKU(book3.getSKU())).thenReturn(Optional.of(book3));
    when(itemDao.getBySKU(book4.getSKU())).thenReturn(Optional.of(book4));

    testObject.setDeliveryDao(deliveryDao);
    testObject.setItemDao(itemDao);

    // Configure resources
    addSingleton(testObject);

  }
  @Test
  public void adminRetrieveDeliverysAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/admin/deliveries")
      .queryParam("ps","1")
      .queryParam("pn", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Delivery list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/delivery/expected-admin-retrieve-deliveries-page-1.json");

    actualResponse = configureAsClient("/admin/deliveries")
      .queryParam("ps","1")
      .queryParam("pn", "1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Delivery list 2 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/delivery/expected-admin-retrieve-deliveries-page-2.json");

  }

  @Test
  public void adminUpdateDeliveryAsHalJson() throws Exception {

    // Starting condition is Alice has {book1: 1, book2: 2}
    // Ending condition is Alice has {book1: 0, book2: 2, book3: 3}

    AdminUpdateDeliveryRequest updateDeliveryRequest = new AdminUpdateDeliveryRequest();
    updateDeliveryRequest.setId(1L);
    // Add a few new items
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItem("0316184136",3));
    // Remove by setting to zero
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItem("0099410672",0));

    String actualResponse = configureAsClient("/admin/deliveries/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateDeliveryRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateDelivery by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/delivery/expected-admin-update-delivery.json");

  }

}
