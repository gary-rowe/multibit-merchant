package org.multibit.mbm.db;

import com.xeiam.xchange.currency.MoneyUtils;
import com.yammer.dropwizard.logging.Log;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.core.model.*;
import org.multibit.mbm.core.pricing.rules.PercentagePricingRule;
import org.multibit.mbm.db.dao.CustomerDao;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.db.dao.UserDao;
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

  // Various Users
  // (saves on DAO access during loading)
  private User trentAdmin = null;
  private User cameronCatalogAdmin = null;
  private User belindaBuyer = null;

  // Supplier
  private User steveSupplier = null;
  private User samSupplier = null;

  // Customer
  private User aliceCustomer = null;
  private User bobCustomer = null;

  // Other
  private User storeClient = null;
  private User anonymous = null;

  // Various Roles that get shared between Users
  // (saves on DAO access during loading)
  private Role adminRole = null;
  private Role buyerRole = null;
  private Role catalogAdminRole = null;
  private Role customerRole = null;
  private Role clientRole = null;
  private Role publicRole = null;
  private Role supplierRole = null;

  // Various Items that get shared between Users
  // (saves on DAO access during loading)
  private Item book1 = null;
  private Item book2 = null;
  private Item book3 = null;
  private Item book4 = null;
  private Item book5 = null;

  /**
   * Handles the process of initialising the database using the DAOs
   */
  public void initialise() {

    LOG.info("Populating database");

    // Build the foundations
    buildRolesAndAuthorities();
    buildUsers();
    buildCatalogBooks();

    // Bind entities together
    buildCustomerCarts();
    buildSupplierDeliveries();

    LOG.info("Complete");

  }

  private void buildRolesAndAuthorities() {

    // Admin role
    adminRole = buildAdminRole();
    adminRole = roleDao.saveOrUpdate(adminRole);

    // Catalog admin
    catalogAdminRole = buildCatalogAdminRole();
    catalogAdminRole = roleDao.saveOrUpdate(catalogAdminRole);

    // Buyer role
    buyerRole = buildBuyerRole();
    buyerRole = roleDao.saveOrUpdate(buyerRole);

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
   * @return A transient instance of the buyer role
   */
  public static Role buildBuyerRole() {
    return RoleBuilder.newInstance()
      .withName(Authority.ROLE_BUYER.name())
      .withDescription("Buyer role")
      .withAuthority(Authority.ROLE_BUYER)
      .build();
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
   */
  private void buildCatalogBooks() {
    book1 = buildBookItemCryptonomicon();

    itemDao.saveOrUpdate(book1);

    book2 = buildBookItemProvence();

    itemDao.saveOrUpdate(book2);

    book3 = buildBookItemPlumbing();

    itemDao.saveOrUpdate(book3);

    book4 = buildBookItemQuantumThief();

    itemDao.saveOrUpdate(book4);

    book5 = buildBookItemCompleteWorks();

    itemDao.saveOrUpdate(book5);

    itemDao.flush();
  }

  /**
   * Build some demonstration carts
   */
  private void buildCustomerCarts() {

    aliceCustomer
      .getCustomer()
      .getCart()
      .setItemQuantity(book1, 1);

    bobCustomer
      .getCustomer()
      .getCart()
      .setItemQuantity(book2, 1);

    userDao.saveOrUpdate(aliceCustomer);
    userDao.saveOrUpdate(bobCustomer);

    userDao.flush();

  }

  /**
   * Build some demonstration deliveries
   */
  private void buildSupplierDeliveries() {

    // Create a bare-bones Delivery
    DeliveryBuilder
      .newInstance()
      .withSupplier(steveSupplier.getSupplier())
      .withDeliveryItem(book1, 1)
      .build();

    DeliveryBuilder
      .newInstance()
      .withSupplier(samSupplier.getSupplier())
      .withDeliveryItem(book2, 2)
      .build();

    userDao.saveOrUpdate(steveSupplier);
    userDao.saveOrUpdate(samSupplier);

    userDao.flush();

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
    // Trent
    trentAdmin = buildTrentAdministrator(adminRole);
    userDao.saveOrUpdate(trentAdmin);

    // Cameron
    cameronCatalogAdmin = buildCameronCatalogAdministrator(catalogAdminRole);
    userDao.saveOrUpdate(cameronCatalogAdmin);

    // Buyers
    // Belinda
    belindaBuyer = buildBelindaBuyer(buyerRole);
    userDao.saveOrUpdate(belindaBuyer);

    // Suppliers
    // Steve
    steveSupplier = buildSteveSupplier(supplierRole);
    userDao.saveOrUpdate(steveSupplier);

    // Sam
    samSupplier = buildSamSupplier(supplierRole);
    userDao.saveOrUpdate(samSupplier);

    // Clients
    storeClient = buildStoreClient(clientRole);
    userDao.saveOrUpdate(storeClient);

    // Customers
    // Alice
    aliceCustomer = buildAliceCustomer(customerRole);
    userDao.saveOrUpdate(aliceCustomer);

    // Bob
    bobCustomer = buildBobCustomer(customerRole);
    userDao.saveOrUpdate(bobCustomer);

    // Public
    anonymous = buildAnonymousPublic(publicRole);
    userDao.saveOrUpdate(anonymous);

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
      .withContactMethod(ContactMethod.EMAIL, "trent@example.org")
      .withRole(adminRole)
      .build();
  }

  public static User buildBelindaBuyer(Role buyerRole) {
    // Admin
    return UserBuilder.newInstance()
      .withApiKey("belinda123")
      .withSecretKey("belinda456")
      .withUsername("belinda")
      .withPassword("belinda1")
      .withContactMethod(ContactMethod.NAMES, "Belinda")
      .withContactMethod(ContactMethod.LAST_NAME, "Buyer")
      .withContactMethod(ContactMethod.EMAIL, "belinda@example.org")
      .withRole(buyerRole)
      .build();
  }

  public static User buildCameronCatalogAdministrator(Role catalogAdminRole) {
    // Catalog Admin
    return UserBuilder.newInstance()
      .withApiKey("cameron123")
      .withSecretKey("cameron456")
      .withUsername("cameron")
      .withPassword("cameron1")
      .withContactMethod(ContactMethod.NAMES, "Cameron")
      .withContactMethod(ContactMethod.LAST_NAME, "Catalog Admin")
      .withContactMethod(ContactMethod.EMAIL, "cameron@example.org")
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
      .withContactMethod(ContactMethod.EMAIL, "steve@example.org")
      .withRole(supplierRole)
      .withSupplier(steveSupplier)
      .build();
  }

  public static User buildSamSupplier(Role supplierRole) {
    Supplier samSupplier = SupplierBuilder.newInstance()
      .build();

    // Catalog Admin
    return UserBuilder.newInstance()
      .withApiKey("sam123")
      .withSecretKey("sam456")
      .withUsername("sam")
      .withPassword("sam1")
      .withContactMethod(ContactMethod.NAMES, "Sam")
      .withContactMethod(ContactMethod.LAST_NAME, "Supplier")
      .withContactMethod(ContactMethod.EMAIL, "sam@example.org")
      .withRole(supplierRole)
      .withSupplier(samSupplier)
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

  public static PricingRule buildPresetMarginPricingRule() {

    PercentagePricingRule pricingRule = new PercentagePricingRule();
    pricingRule.setPercentage(1.2);

    return pricingRule;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
