package org.multibit.mbm.resources.delivery;

import com.google.common.base.Optional;
import org.junit.Test;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.delivery.SupplierDeliveryItem;
import org.multibit.mbm.api.request.delivery.SupplierUpdateDeliveryRequest;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dao.DeliveryDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Delivery;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.Supplier;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierDeliveryResourceTest extends BaseJerseyHmacResourceTest {

  private final DeliveryDao deliveryDao=mock(DeliveryDao.class);
  private final ItemDao itemDao=mock(ItemDao.class);

  private final SupplierDeliveryResource testObject=new SupplierDeliveryResource();

  @Override
  protected void setUpResources() {

    // Use Alice for Supplier access
    User supplierUser = setUpSteveHmacAuthenticator();
    supplierUser.setId(1L);

    Supplier supplier = supplierUser.getSupplier();
    supplier.setId(1L);

    // Configure the Delivery with Items
    Delivery supplierDelivery = supplier.getDeliveries().iterator().next();
    supplierDelivery.setId(1L);

    Item book1 = DatabaseLoader.buildBookItemCryptonomicon();
    book1.setId(1L);
    Item book2 = DatabaseLoader.buildBookItemQuantumThief();
    book2.setId(2L);
    Item book3 = DatabaseLoader.buildBookItemCompleteWorks();
    book3.setId(3L);
    Item book4 = DatabaseLoader.buildBookItemPlumbing();
    book4.setId(4L);

    // Include some books into the Supplier Delivery
    supplierDelivery.setItemQuantity(book1,1);
    supplierDelivery.setItemQuantity(book2,2);

    // Configure Delivery DAO
    when(deliveryDao.saveOrUpdate(supplierDelivery)).thenReturn(supplierDelivery);

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
  public void retrieveDeliveryAsHalJson() throws Exception {

    String actualResponse = configureAsClient("/delivery")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .get(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("Supplier retrieve Delivery as HAL+JSON", actualResponse, "/fixtures/hal/delivery/expected-supplier-retrieve-delivery.json");

  }

  @Test
  public void updateDeliveryAsHalJson() throws Exception {

    // Starting condition is Supplier has {book1: 1, book2: 2}
    // Ending condition is Supplier has {book1: 0, book2: 2, book3: 3}

    SupplierUpdateDeliveryRequest updateDeliveryRequest = new SupplierUpdateDeliveryRequest();
    // Add a few new items
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItem("0316184136",3));
    // Remove by setting to zero
    updateDeliveryRequest.getDeliveryItems().add(new SupplierDeliveryItem("0099410672",0));

    String actualResponse = configureAsClient("/delivery")
      .accept(HalMediaType.APPLICATION_HAL_JSON)
      .entity(updateDeliveryRequest, MediaType.APPLICATION_JSON_TYPE)
      .put(String.class);

    FixtureAsserts.assertStringMatchesJsonFixture("UpdateDelivery by admin response render to HAL+JSON",actualResponse, "/fixtures/hal/delivery/expected-supplier-update-delivery.json");

  }


}
