package org.multibit.mbm.db;

import com.xeiam.xchange.utils.MoneyUtils;
import com.yammer.dropwizard.logging.Log;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dao.UserDao;
import org.multibit.mbm.db.dto.*;
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

  // Various Roles that get shared between Users
  private Role adminRole = null;
  private Role catalogAdminRole = null;
  private Role customerRole = null;
  private Role clientRole = null;
  private Role publicRole = null;
  private Role supplierRole = null;

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

    // Admin role
    adminRole = buildAdminRole();
    adminRole = roleDao.saveOrUpdate(adminRole);

    // Catalog admin
    catalogAdminRole = buildCatalogAdminRole();
    catalogAdminRole = roleDao.saveOrUpdate(catalogAdminRole);

    // Build the Customer Role and Authorities
    customerRole = buildCustomerRole();
    customerRole = roleDao.saveOrUpdate(customerRole);

    // Build the Client Role and Authorities
    clientRole = buildClientRole();
    clientRole = roleDao.saveOrUpdate(clientRole);

    // Build the Public Role and Authorities
    publicRole = buildPublicRole();
    publicRole = roleDao.saveOrUpdate(publicRole);

    // Build the Supplier Role and Authorities
    supplierRole = buildSupplierRole();
    supplierRole = roleDao.saveOrUpdate(supplierRole);

    roleDao.flush();

  }

  /**
   * @return A transient instance of the catalog administrator role
   */
  public static Role buildCatalogAdminRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_CATALOG_ADMIN.name())
      .withDescription("Catalog administrator role")
      .withAuthority(Authority.ROLE_CATALOG_ADMIN)
      .build();
  }

  /**
   * @return A transient instance of the anonymous public role
   */
  public static Role buildPublicRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_PUBLIC.name())
      .withDescription("Public role")
      .withPublicAuthorities()
      .build();
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
   * @return A transient instance of the client role
   */
  public static Role buildClientRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_CLIENT.name())
      .withDescription("Client role")
      .withClientAuthorities()
      .build();
  }

  /**
   * @return A transient instance of the Supplier role
   */
  public static Role buildSupplierRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_SUPPLIER.name())
      .withDescription("Supplier role")
      .withSupplierAuthorities()
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
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 2.2"))
      .withPrimaryFieldDetail(ItemField.TITLE, "A Year In Provence", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Peter Mayle", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's bestselling, much-loved account of 'A Year In Provence'.", "en")
      .withPrimaryFieldDetail(ItemField.GENRE, "Comedy", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "http://multibit-store.herokuapp.com/images/book.jpg", "en")
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
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 3.3"))
      .withPrimaryFieldDetail(ItemField.TITLE, "Plumbing and Central Heating", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Mike Lawrence", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation, from mending a leaking tap to installing a new shower unit.", "en")
      .withPrimaryFieldDetail(ItemField.GENRE, "DIY", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "http://multibit-store.herokuapp.com/images/book.jpg", "en")
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
      .withGTIN("978-0575088894")
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 4.4"))
      .withPrimaryFieldDetail(ItemField.TITLE, "The Quantum Thief", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Hannu Rajaniemi", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard Morgan.", "en")
      .withPrimaryFieldDetail(ItemField.GENRE, "Sci-Fi", "en")
      .withPrimaryFieldDetail(ItemField.PUBLISHER, "Gollancz (1 Nov 2011)", "en")
      .withPrimaryFieldDetail(ItemField.FORMAT, "Paperback", "en")
      .withPrimaryFieldDetail(ItemField.PAGE_COUNT, "336", "en")
      .withPrimaryFieldDetail(ItemField.DESCRIPTION, "<p>The Quantum Thief is a dazzling hard SF novel set in the solar system of the far future - a heist novel peopled by bizarre post-humans but powered by very human motives of betrayal, revenge and jealousy. It is a stunning debut.</p><br/>" +
        "<p>Jean le Flambeur is a post-human criminal, mind burglar, confidence artist and trickster. His origins are shrouded in mystery, but his exploits are known throughout the Heterarchy - from breaking into the vast Zeusbrains of the Inner System to steal their thoughts, to stealing rare Earth antiques from the aristocrats of the Moving Cities of Mars.</p>" +
        "<p>Except that Jean made one mistake. Now he is condemned to play endless variations of a game-theoretic riddle in the vast virtual jail of the Axelrod Archons - the Dilemma Prison - against countless copies of himself.</p>" +
        "<p>Jean's routine of death, defection and cooperation is upset by the arrival of Mieli and her spidership, Perhonen. She offers him a chance to win back his freedom and the powers of his old self - in exchange for finishing the one heist he never quite managed...</p>", "en")
      .withPrimaryFieldDetail(ItemField.SIZE, "12.9 x 2.1 x 19.6 cm", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "http://multibit-store.herokuapp.com/images/book.jpg", "en")
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
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 5.5"))
      .withPrimaryFieldDetail(ItemField.TITLE, "The Complete Works of Emily Dickinson", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Emily Dickinson, edited by Thomas H Johnson", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's poems.", "en")
      .withPrimaryFieldDetail(ItemField.GENRE, "Poetry", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "http://multibit-store.herokuapp.com/images/book.jpg", "en")
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
      .withLocalPrice(MoneyUtils.parseBitcoin("BTC 1.1"))
      .withPrimaryFieldDetail(ItemField.TITLE, "Cryptonomicon", "en")
      .withPrimaryFieldDetail(ItemField.AUTHOR, "Neal Stephenson", "en")
      .withPrimaryFieldDetail(ItemField.SUMMARY, "'A brilliant patchwork of code-breaking mathematicians and their descendants who are striving to create a data haven in the Philippines...trust me on this one' Guardian", "en")
      .withPrimaryFieldDetail(ItemField.GENRE, "Sci-Fi", "en")
      .withPrimaryFieldDetail(ItemField.IMAGE_THUMBNAIL_URI, "http://multibit-store.herokuapp.com/images/book.jpg", "en")
      .build();
  }

  private void buildUsers() {

    // Administrators
    User userTrent = buildTrentAdministrator(adminRole);
    userDao.saveOrUpdate(userTrent);

    User userSam = buildSamCatalogAdministrator(catalogAdminRole);
    userDao.saveOrUpdate(userSam);

    // TODO Introduce various staff roles (dispatch, sales etc)

    // Clients
    User userStore = buildStoreClient(clientRole);
    userDao.saveOrUpdate(userStore);

    // Customers
    // Alice
    User userAlice = buildAliceCustomer(customerRole);
    userDao.saveOrUpdate(userAlice);

    // Bob
    User bob = buildBobCustomer(customerRole);
    userDao.saveOrUpdate(bob);

    // Public
    User anonymous = buildAnonymousPublic(publicRole);
    userDao.saveOrUpdate(anonymous);

    // Suppliers
    // Steve
    User steve = buildSteveSupplier(supplierRole);
    userDao.saveOrUpdate(steve);

    userDao.flush();

  }

  public static User buildAnonymousPublic(Role publicRole) {
    Customer anonymousCustomer = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withApiKey("anonymous123")
      .withSecretKey("anonymous456")
      .withUsername("")
      .withPassword("")
      .withRole(publicRole)
      .withCustomer(anonymousCustomer)
      .build();
  }

  public static User buildBobCustomer(Role customerRole) {
    Customer bobCustomer = CustomerBuilder.newInstance()
      .build();

    return UserBuilder.newInstance()
      .withApiKey("bob123")
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
      .withApiKey("alice123")
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
      .withApiKey("trent123")
      .withSecretKey("trent456")
      .withUsername("trent")
      .withPassword("trent1")
      .withContactMethod(ContactMethod.NAMES, "Trent")
      .withContactMethod(ContactMethod.LAST_NAME, "Admin")
      .withContactMethod(ContactMethod.EMAIL, "admin@example.org")
      .withRole(adminRole)
      .build();
  }

  public static User buildSamCatalogAdministrator(Role catalogAdminRole) {
    // Catalog Admin
    return UserBuilder.newInstance()
      .withApiKey("sam123")
      .withSecretKey("sam456")
      .withUsername("sam")
      .withPassword("sam1")
      .withContactMethod(ContactMethod.NAMES, "Sam")
      .withContactMethod(ContactMethod.LAST_NAME, "Catalog Admin")
      .withContactMethod(ContactMethod.EMAIL, "catalog.admin@example.org")
      .withRole(catalogAdminRole)
      .build();
  }

  public static User buildSteveSupplier(Role supplierRole) {
    Supplier steveSupplier = SupplierBuilder.newInstance()
      .build();

    // Catalog Admin
    return UserBuilder.newInstance()
      .withApiKey("steve123")
      .withSecretKey("steve456")
      .withUsername("steve")
      .withPassword("steve1")
      .withContactMethod(ContactMethod.NAMES, "Steve")
      .withContactMethod(ContactMethod.LAST_NAME, "Supplier")
      .withContactMethod(ContactMethod.EMAIL, "supplier@example.org")
      .withRole(supplierRole)
      .withSupplier(steveSupplier)
      .build();
  }

  public static User buildStoreClient(Role clientRole) {
    // Admin
    return UserBuilder.newInstance()
      .withApiKey("store123")
      .withSecretKey("store456")
      .withUsername("store")
      .withPassword("store1")
      .withContactMethod(ContactMethod.NAMES, "Store")
      .withContactMethod(ContactMethod.LAST_NAME, "Client")
      .withContactMethod(ContactMethod.EMAIL, "store@example.org")
      .withRole(clientRole)
      .build();
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }

}
