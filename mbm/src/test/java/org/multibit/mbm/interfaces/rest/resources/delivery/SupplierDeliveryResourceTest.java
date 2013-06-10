package org.multibit.mbm.interfaces.rest.resources.delivery;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.interfaces.rest.api.delivery.SupplierDeliveryItemDto;
import org.multibit.mbm.interfaces.rest.api.delivery.SupplierUpdateDeliveryDto;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.infrastructure.persistence.DatabaseLoader;
import org.multibit.mbm.domain.repositories.DeliveryReadService;
import org.multibit.mbm.domain.repositories.ItemReadService;
import org.multibit.mbm.testing.BaseJerseyHmacResourceTest;
import org.multibit.mbm.testing.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierDeliveryResourceTest extends BaseJerseyHmacResourceTest {

  private final DeliveryReadService deliveryReadService =mock(DeliveryReadService.class);
  private final ItemReadService itemReadService =mock(ItemReadService.class);

  private final SupplierDeliveryResource testObject=new SupplierDeliveryResource();

  @Override
  protected void setUpResources() {

    // Use Alice for Supplier access
    User supplierUser = setUpSteveHmacAuthenticator();
    supplierUser.setId(1L);

    Supplier supplier = supplierUser.getSupplier();
    supplier.setId(1L);

    // Configure the Delivery with Items
    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);
    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    // Include some books into the Supplier Delivery
    Delivery supplierDelivery = DeliveryBuilder
      .newInstance()
      .withSupplier(supplier)
      .withDeliveryItem(book1,1)
      .withDeliveryItem(book2,2)
      .build();
    supplierDelivery.setId(1L);
    supplier.getDeliveries().add(supplierDelivery);

    // Configure Delivery DAO
    when(deliveryReadService.saveOrUpdate(supplierDelivery)).thenReturn(supplierDelivery);

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
  public void retrieveDeliveryAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/supplier/delivery/1234")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Supplier retrieve Delivery as HAL+JSON", actualResponse, "/fixtures/hal/delivery/expected-supplier-retrieve-delivery.json");

  }

  @Test
  public void updateDeliveryAsHalJson() throws Exception {

    // Starting condition is Supplier has {book1: 1, book2: 2}
    // Ending condition is Supplier has {book1: 0, book2: 2, book3: 3}

    SupplierUpdateDeliveryDto updateDeliveryRequest = new SupplierUpdateDeliveryDto();
    // Add a few new items
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItemDto("0316184136",3));
    // Remove by setting to zero
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItemDto("0099410672",0));

    String actualResponse = configureAsClient(SupplierDeliveryResource.class)
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateDeliveryRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateDelivery by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/delivery/expected-supplier-update-delivery.json");

  }


}
