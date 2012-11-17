package org.multibit.mbm.resources.delivery;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.delivery.AdminUpdateDeliveryRequest;
import org.multibit.mbm.api.request.delivery.SupplierDeliveryItem;
import org.multibit.mbm.core.model.*;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.Set;

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

    // Configure Steve Supplier
    User steveUser = DatabaseLoader.buildSteveSupplier(supplierRole);
    steveUser.setId(1L);
    steveUser.getSupplier().setId(1L);

    // Configure Steve's Delivery with Items
    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);

    Delivery steveDelivery1 = DeliveryBuilder
      .newInstance()
      .withSupplier(steveUser.getSupplier())
      .withDeliveryItem(book1, 1)
      .withDeliveryItem(book2,2)
      .build();
    steveDelivery1.setId(1L);
    steveUser.getSupplier().getDeliveries().add(steveDelivery1);

    // Configure Sam Supplier
    User samUser = DatabaseLoader.buildSamSupplier(supplierRole);
    samUser.setId(1L);
    samUser.getSupplier().setId(1L);

    // Configure Sam's Delivery with Items
    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    Delivery samDelivery1 = DeliveryBuilder
      .newInstance()
      .withSupplier(samUser.getSupplier())
      .withDeliveryItem(book3, 3)
      .withDeliveryItem(book4,4)
      .build();
    samDelivery1.setId(1L);
    samUser.getSupplier().getDeliveries().add(samDelivery1);

    // Create some mock results
    Set<Delivery> steveDeliveries = Sets.newHashSet(steveUser.getSupplier().getDeliveries());
    Set<Delivery> samDeliveries = Sets.newHashSet(samUser.getSupplier().getDeliveries());

    // Configure Delivery DAO
    when(deliveryDao.getById(steveDelivery1.getId())).thenReturn(Optional.of(steveDelivery1));
    when(deliveryDao.getById(samDelivery1.getId())).thenReturn(Optional.of(samDelivery1));
    when(deliveryDao.getAllByPage(1, 0)).thenReturn(Lists.newLinkedList(steveDeliveries));
    when(deliveryDao.getAllByPage(1, 1)).thenReturn(Lists.newLinkedList(samDeliveries));
    when(deliveryDao.saveOrUpdate(steveDelivery1)).thenReturn(steveDelivery1);
    when(deliveryDao.saveOrUpdate(samDelivery1)).thenReturn(samDelivery1);

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

    String actualResponse = configureAsClient(AdminDeliveryResource.class)
      .queryParam("ps","1")
      .queryParam("pn", "0")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Delivery list 1 can be retrieved as HAL+JSON", actualResponse, "/fixtures/hal/delivery/expected-admin-retrieve-deliveries-page-1.json");

    actualResponse = configureAsClient(AdminDeliveryResource.class)
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
