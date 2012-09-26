package org.multibit.mbm.db;

import org.multibit.mbm.db.dto.ItemBuilder;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemField;
import org.multibit.mbm.db.dto.CustomerBuilder;
import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.RoleBuilder;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.ContactMethod;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  <p>Loads the database to provide default standard data to the application:</p>
 *
 * TODO Move this into the test source tree after the database has been stabilised
 *
 * @since 0.0.1
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

  @Resource(name = "hibernateRoleDao")
  private RoleDao roleDao;

  private Role adminRole = null;

  private Role customerRole = null;

  /**
   * Handles the process of initialising the database using the DAOs
   */
  public void initialise() {

    log.info("Populating database");

    buildRolesAndAuthorities();
    buildUsers();
    buildCatalogBooks();

    log.info("Complete");

  }

  private void buildRolesAndAuthorities() {

    adminRole = buildRoleAdmin();
    adminRole = roleDao.saveOrUpdate(adminRole);

    // Build the Customer Role and Authorities
    customerRole = buildRoleCustomer();
    customerRole = roleDao.saveOrUpdate(customerRole);

    roleDao.flush();

  }

  public static Role buildRoleCustomer() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_CUSTOMER.name())
      .withDescription("Customer role")
      .withCustomerAuthorities()
      .build();
  }

  public static Role buildRoleAdmin() {
    // Build the administration Role and Authorities
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_ADMIN.name())
      .withDescription("Administration role")
      .withAdminAuthorities()
      .build();
  }

  /**
   * Build a demonstration database based on books
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
   * @return A populated book item
   */
  public static Item buildBookItemProvence() {
    return ItemBuilder.newInstance()
        .withSKU("0140296034")
        .withPrimaryFieldDetail(ItemField.TITLE, "A Year In Provence, by Peter Mayle", "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, "Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's bestselling, much-loved account of 'A Year In Provence'.", "en")
        .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/1/thumbnail1.png", "en")
        .build();
  }

  /**
   * @return A populated book item
   */
  public static Item buildBookItemPlumbing() {
    return ItemBuilder.newInstance()
        .withSKU("186126173X")
        .withPrimaryFieldDetail(ItemField.TITLE, "Plumbing and Central Heating, by Mike Lawrence", "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, "This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation, from mending a leaking tap to installing a new shower unit.", "en")
        .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/3/thumbnail3.png", "en")
        .build();
  }

  /**
   * @return A populated book item
   */
  public static Item buildBookItemQuantumThief() {
    return ItemBuilder.newInstance()
        .withSKU("0575088893")
        .withPrimaryFieldDetail(ItemField.TITLE, "The Quantum Thief, by Hannu Rajaniemi", "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, "The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard Morgan.", "en")
        .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/4/thumbnail4.png", "en")
        .build();
  }

  /**
   * @return A populated book item
   */
  public static Item buildBookItemCompleteWorks() {
    return ItemBuilder.newInstance()
        .withSKU("0316184136")
        .withPrimaryFieldDetail(ItemField.TITLE, "The Complete Works of Emily Dickinson, edited by Thomas H Johnson", "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, "The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's poems.", "en")
        .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/5/thumbnail5.png", "en")
        .build();
  }

  /**
   * @return A populated book item
   */
  public static Item buildBookItemCryptonomicon() {
    return ItemBuilder.newInstance()
        .withSKU("0099410672")
        .withPrimaryFieldDetail(ItemField.TITLE, "Cryptonomicon, by Neal Stephenson", "en")
        .withPrimaryFieldDetail(ItemField.SUMMARY, "'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian", "en")
        .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "/mbm/images/catalog/items/2/thumbnail2.png", "en")
        .build();
  }

  private void buildUsers() {

    User userTrent = buildUserTrent(adminRole);
    userDao.saveOrUpdate(userTrent);

    // TODO Introduce various staff roles (dispatch, sales etc)

    // Customers
    // Alice
    User userAlice = buildCustomerAlice(customerRole);

    userDao.saveOrUpdate(userAlice);

    // Bob
    User bob = buildCustomerBob(customerRole);

    userDao.saveOrUpdate(bob);

    userDao.flush();

  }

  public static User buildCustomerBob(Role customerRole) {
    Customer bobCustomer = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withUUID("bob123")
      .withUsername("bob")
      .withPassword("bob1")
      .withContactMethod(ContactMethod.FIRST_NAME, "Bob")
      .withContactMethod(ContactMethod.LAST_NAME, "Customer")
      .withContactMethod(ContactMethod.EMAIL, "bob@example.org")
      .withRole(customerRole)
      .build();
  }

  public static User buildCustomerAlice(Role customerRole) {
    Customer customerAlice = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withUUID("alice123")
      .withUsername("alice")
      .withPassword("alice1")
      .withContactMethod(ContactMethod.FIRST_NAME, "Alice")
      .withContactMethod(ContactMethod.LAST_NAME, "Customer")
      .withContactMethod(ContactMethod.EMAIL, "alice@example.org")
      .withRole(customerRole)
      .withCustomer(customerAlice)
      .build();
  }

  public static User buildUserTrent(Role adminRole) {
    // Admin
    return UserBuilder.newInstance()
      .withUUID("trent123")
      .withUsername("trent")
      .withPassword("trent1")
      .withContactMethod(ContactMethod.FIRST_NAME, "Trent")
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
