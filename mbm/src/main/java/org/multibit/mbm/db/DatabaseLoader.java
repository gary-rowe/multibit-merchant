package org.multibit.mbm.db;

import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.*;
import com.yammer.dropwizard.logging.Log;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  <p>Loads the database to provide default standard data to the application:</p>
 *
 * @since 0.0.1
 *         
 */
@Component
public class DatabaseLoader {

  private static final Log LOG = Log.forClass(DatabaseLoader.class);

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  @Resource(name = "hibernateCustomerDao")
  private CustomerDao customerDao;

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  @Resource(name = "hibernateRoleDao")
  private RoleDao roleDao;

  private Role adminRole = null;

  private Role customerRole = null;

  /**
   * Handles the process of initialising the database using the DAOs
   */
  public void initialise() {

    LOG.info("Populating database");

    buildRolesAndAuthorities();
    buildUsers();
    buildCatalogBooks();

    LOG.info("Complete");

  }

  private void buildRolesAndAuthorities() {

    adminRole = buildAdminRole();
    adminRole = roleDao.saveOrUpdate(adminRole);

    // Build the Customer Role and Authorities
    customerRole = buildCustomerRole();
    customerRole = roleDao.saveOrUpdate(customerRole);

    roleDao.flush();

  }

  /**
   * @return A transient instance of the Customer role
   */
  public static Role buildCustomerRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_CUSTOMER.name())
      .withDescription("Customer role")
      .withCustomerAuthorities()
      .build();
  }

  /**
   * @return A transient instance of the Administrator role
   */
  public static Role buildAdminRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_ADMIN.name())
      .withDescription("Administration role")
      .withAdminAuthorities()
      .build();
  }

  /**
   * Build a demonstration database based on books
   * TODO Refactor into BookStore, MusicStore etc
   */
  private void buildCatalogBooks() {
    Item book1 = buildBookItemCryptonomicon();

    itemDao.saveOrUpdate(book1);

    Item book2 = buildBookItemProvence();

    itemDao.saveOrUpdate(book2);

    Item book3 = buildBookItemPlumbing();

    itemDao.saveOrUpdate(book3);

    Item book4 = buildBookItemQuantumThief();

    itemDao.saveOrUpdate(book4);

    Item book5 = buildBookItemCompleteWorks();

    itemDao.saveOrUpdate(book5);

    itemDao.flush();
  }

  /**
   * Book: "A Year In Provence"
   *
   * @return A transient instance of the Item
   */
  public static Item buildBookItemProvence() {
    return ItemBuilder.newInstance()
      .withSKU("0140296034")
      .withPrimaryFieldDetail(ItemField.TITLE, "A Year In Provence", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Peter Mayle", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's bestselling, much-loved account of 'A Year In Provence'.", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/1/thumbnail1.png", "en")
      .build();
  }

  /**
   * Book: "Plumbing and Central Heating"
   *
   * @return A transient instance of the Item
   */
  public static Item buildBookItemPlumbing() {
    return ItemBuilder.newInstance()
      .withSKU("186126173X")
      .withPrimaryFieldDetail(ItemField.TITLE, "Plumbing and Central Heating", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Mike Lawrence", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation, from mending a leaking tap to installing a new shower unit.", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/3/thumbnail3.png", "en")
      .build();
  }

  /**
   * Book: "The Quantum Thief"
   *
   * @return A transient instance of the Item
   */
  public static Item buildBookItemQuantumThief() {
    return ItemBuilder.newInstance()
      .withSKU("0575088893")
      .withPrimaryFieldDetail(ItemField.TITLE, "The Quantum Thief", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Hannu Rajaniemi", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard Morgan.", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/4/thumbnail4.png", "en")
      .build();
  }


  /**
   * Book: "The Complete Works of Emily Dickinson"
   *
   * @return A transient instance of the Item
   */
  public static Item buildBookItemCompleteWorks() {
    return ItemBuilder.newInstance()
      .withSKU("0316184136")
      .withPrimaryFieldDetail(ItemField.TITLE, "The Complete Works of Emily Dickinson", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Emily Dickinson, edited by Thomas H Johnson", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's poems.", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/5/thumbnail5.png", "en")
      .build();
  }

  /**
   * Book: "Cryptonomicon"
   *
   * @return A transient instance of the Item
   */
  public static Item buildBookItemCryptonomicon() {
    return ItemBuilder.newInstance()
      .withSKU("0099410672")
      .withPrimaryFieldDetail(ItemField.TITLE, "Cryptonomicon", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Neal Stephenson", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/2/thumbnail2.png", "en")
      .build();
  }

  private void buildUsers() {

    User userTrent = buildTrentAdministrator(adminRole);
    userDao.saveOrUpdate(userTrent);

    // TODO Introduce various staff roles (dispatch, sales etc)

    // Customers
    // Alice
    User userAlice = buildAliceCustomer(customerRole);
    userDao.saveOrUpdate(userAlice);

    // Bob
    User bob = buildBobCustomer(customerRole);
    userDao.saveOrUpdate(bob);

    userDao.flush();

  }

  public static User buildBobCustomer(Role customerRole) {
    Customer bobCustomer = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withUUID("bob123")
      .withSecretKey("bob456")
      .withUsername("bob")
      .withPassword("bob1")
      .withContactMethod(ContactMethod.NAMES, "Bob")
      .withContactMethod(ContactMethod.LAST_NAME, "Customer")
      .withContactMethod(ContactMethod.EMAIL, "bob@example.org")
      .withRole(customerRole)
      .withCustomer(bobCustomer)
      .build();
  }

  public static User buildAliceCustomer(Role customerRole) {
    Customer aliceCustomer = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withUUID("alice123")
      .withSecretKey("alice456")
      .withUsername("alice")
      .withPassword("alice1")
      .withContactMethod(ContactMethod.NAMES, "Alice")
      .withContactMethod(ContactMethod.LAST_NAME, "Customer")
      .withContactMethod(ContactMethod.EMAIL, "alice@example.org")
      .withRole(customerRole)
      .withCustomer(aliceCustomer)
      .build();
  }

  public static User buildTrentAdministrator(Role adminRole) {
    // Admin
    return UserBuilder.newInstance()
      .withUUID("trent123")
      .withSecretKey("trent456")
      .withUsername("trent")
      .withPassword("trent1")
      .withContactMethod(ContactMethod.NAMES, "Trent")
      .withContactMethod(ContactMethod.LAST_NAME, "Admin")
      .withContactMethod(ContactMethod.EMAIL, "admin@example.org")
      .withRole(adminRole)
      .build();
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
