package org.multibit.demo;

import org.multibit.mbm.catalog.builder.ItemBuilder;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  <p>Loads the database to provide default standard data to the application:</p>
 *
 * @since 1.0.0
 *         
 */
@Component
public class DatabaseLoader {

  private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  @Resource(name = "hibernateCustomerDao")
  private CustomerDao customerDao;

  /**
   * Handles the process of initialising the database using the DAOs
   */
  public void initialise() {

    log.info("Populating database");

    buildCustomerAdmin();
    buildCatalogBooks();

    log.info("Complete");

  }

  /**
   * Build a demonstration database based on books
   */
  private void buildCatalogBooks() {
    Item book1 = ItemBuilder.getInstance()
      .setReference("0099410672")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","Cryptonomicon, by Neal Stephenson")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/2/thumbnail2.png")
      .build();

    itemDao.persist(book1);

  }

  private void buildCustomerAdmin() {
    Customer admin = new Customer();
    admin.setOpenId("abc123");
    ContactMethodDetail adminCmd = new ContactMethodDetail();
    adminCmd.setPrimaryDetail("admin@example.org");
    admin.setContactMethodDetail(ContactMethod.EMAIL, adminCmd);

    customerDao.persist(admin);
  }

}
