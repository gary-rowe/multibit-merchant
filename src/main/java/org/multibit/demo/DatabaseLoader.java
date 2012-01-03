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
      .setSku("0099410672")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","Cryptonomicon, by Neal Stephenson")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/2/thumbnail2.png")
      .build();

    itemDao.persist(book1);

    Item book2 = ItemBuilder.getInstance()
      .setSku("0140296034")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","A Year In Provence, by Peter Mayle")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's bestselling, much-loved account of 'A Year In Provence'.")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/1/thumbnail1.png")
      .build();

    itemDao.persist(book2);

    Item book3 = ItemBuilder.getInstance()
      .setSku("186126173X")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","Plumbing and Central Heating, by Mike Lawrence")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation, from mending a leaking tap to installing a new shower unit.")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/3/thumbnail3.png")
      .build();

    itemDao.persist(book3);

    Item book4 = ItemBuilder.getInstance()
      .setSku("0575088893")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","The Quantum Thief, by Hannu Rajaniemi")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard Morgan.")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/4/thumbnail4.png")
      .build();

    itemDao.persist(book4);

    Item book5 = ItemBuilder.getInstance()
      .setSku("0316184136")
      .addPrimaryFieldDetail(ItemField.TITLE,"en","The Complete Works of Emily Dickinson, edited by Thomas H Johnson")
      .addPrimaryFieldDetail(ItemField.SUMMARY,"en","The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's poems.")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI,"en","/mbm/images/catalog/items/5/thumbnail5.png")
      .build();

    itemDao.persist(book5);

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
