package org.multibit.mbm.interfaces.rest.resources.delivery;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.common.pagination.PaginatedLists;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.domain.repositories.DeliveryReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.interfaces.rest.api.delivery.AdminUpdateDeliveryDto;
import org.multibit.mbm.interfaces.rest.api.delivery.SupplierDeliveryItemDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminDeliveryResourceTest extends BaseJerseyHmacResourceTest {

  private final DeliveryReadService deliveryReadService =mock(DeliveryReadService.class);
  private final ItemReadService itemReadService =mock(ItemReadService.class);

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

    PaginatedList<Delivery> page1 = PaginatedLists.newPaginatedArrayList(1, 2, steveDeliveries);
    PaginatedList<Delivery> page2 = PaginatedLists.newPaginatedArrayList(2, 2, samDeliveries);

    when(deliveryReadService.getById(steveDelivery1.getId())).thenReturn(Optional.of(steveDelivery1));
    when(deliveryReadService.getById(samDelivery1.getId())).thenReturn(Optional.of(samDelivery1));
    when(deliveryReadService.getPaginatedList(1, 0)).thenReturn(page1);
    when(deliveryReadService.getPaginatedList(1, 1)).thenReturn(page2);
    when(deliveryReadService.saveOrUpdate(steveDelivery1)).thenReturn(steveDelivery1);
    when(deliveryReadService.saveOrUpdate(samDelivery1)).thenReturn(samDelivery1);

    // Configure Item DAO
    when(itemReadService.getBySKU(book1.getSKU())).thenReturn(Optional.of(book1));
    when(itemReadService.getBySKU(book2.getSKU())).thenReturn(Optional.of(book2));
    when(itemReadService.getBySKU(book3.getSKU())).thenReturn(Optional.of(book3));
    when(itemReadService.getBySKU(book4.getSKU())).thenReturn(Optional.of(book4));

    testObject.setDeliveryReadService(deliveryReadService);
    testObject.setItemReadService(itemReadService);

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

    AdminUpdateDeliveryDto updateDeliveryRequest = new AdminUpdateDeliveryDto();
    updateDeliveryRequest.setId(1L);
    // Add a few new items
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItemDto("0316184136",3));
    // Remove by setting to zero
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItemDto("0099410672",0));

    String actualResponse = configureAsClient("/admin/deliveries/1")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateDeliveryRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateDelivery by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/delivery/expected-admin-update-delivery.json");

  }

}
