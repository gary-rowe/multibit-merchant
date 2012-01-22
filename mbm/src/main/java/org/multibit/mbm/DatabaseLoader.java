package org.multibit.mbm;

import org.multibit.mbm.catalog.builder.ItemBuilder;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.customer.dao.CustomerDao;
import org.multibit.mbm.security.builder.UserBuilder;
import org.multibit.mbm.security.dao.UserDao;
import org.multibit.mbm.security.dto.ContactMethod;
import org.multibit.mbm.security.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  <p>Loads the database to provide default standard data to the application:</p>
 *
 * TODO Move this into the test source tree after the database has been stabilised
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

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  /**
   * Handles the process of initialising the database using the DAOs
   */
  public void initialise() {

    log.info("Populating database");

    buildUsers();
    buildCatalogBooks();

    log.info("Complete");

  }

  /**
   * Build a demonstration database based on books
   */
  private void buildCatalogBooks() {
    Item book1 = ItemBuilder.getInstance()
      .setSKU("0099410672")
      .addPrimaryFieldDetail(ItemField.TITLE, "Cryptonomicon, by Neal Stephenson", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian", "en")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/2/thumbnail2.png", "en")
      .build();

    itemDao.saveOrUpdate(book1);

    Item book2 = ItemBuilder.getInstance()
      .setSKU("0140296034")
      .addPrimaryFieldDetail(ItemField.TITLE, "A Year In Provence, by Peter Mayle", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's bestselling, much-loved account of 'A Year In Provence'.", "en")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/1/thumbnail1.png", "en")
      .build();

    itemDao.saveOrUpdate(book2);

    Item book3 = ItemBuilder.getInstance()
      .setSKU("186126173X")
      .addPrimaryFieldDetail(ItemField.TITLE, "Plumbing and Central Heating, by Mike Lawrence", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation, from mending a leaking tap to installing a new shower unit.", "en")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/3/thumbnail3.png", "en")
      .build();

    itemDao.saveOrUpdate(book3);

    Item book4 = ItemBuilder.getInstance()
      .setSKU("0575088893")
      .addPrimaryFieldDetail(ItemField.TITLE, "The Quantum Thief, by Hannu Rajaniemi", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard Morgan.", "en")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/4/thumbnail4.png", "en")
      .build();

    itemDao.saveOrUpdate(book4);

    Item book5 = ItemBuilder.getInstance()
      .setSKU("0316184136")
      .addPrimaryFieldDetail(ItemField.TITLE, "The Complete Works of Emily Dickinson, edited by Thomas H Johnson", "en")
      .addPrimaryFieldDetail(ItemField.SUMMARY, "The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's poems.", "en")
      .addPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/5/thumbnail5.png", "en")
      .build();

    itemDao.saveOrUpdate(book5);

    itemDao.flush();
  }

  private void buildUsers() {
    // Admin

    User admin = UserBuilder.getInstance()
      .setUUID("trent123")
      .setUsername("trent")
      .setPassword("trent")
      .addContactMethod(ContactMethod.FIRST_NAME, "Trent")
      .addContactMethod(ContactMethod.EMAIL,"admin@example.org")
      .configureAsAdmin()
      .build();

    userDao.saveOrUpdate(admin);

    // TODO Introduce various staff roles

    // Customers
    // Alice
    User alice = UserBuilder.getInstance()
      .setUUID("alice123")
      .setUsername("alice")
      .setPassword("alice")
      .addContactMethod(ContactMethod.FIRST_NAME, "Alice")
      .addContactMethod(ContactMethod.LAST_NAME, "Customer")
      .addContactMethod(ContactMethod.EMAIL,"alice@example.org")
      .configureAsCustomer()
      .build();


    userDao.saveOrUpdate(alice);

    // Bob
    User bob = UserBuilder.getInstance()
      .setUUID("bob123")
      .setUsername("bob")
      .setPassword("bob")
      .addContactMethod(ContactMethod.FIRST_NAME, "Bob")
      .addContactMethod(ContactMethod.LAST_NAME, "Customer")
      .addContactMethod(ContactMethod.EMAIL,"bob@example.org")
      .configureAsCustomer()
      .build();

    userDao.saveOrUpdate(bob);

    userDao.flush();

  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
