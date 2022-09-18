package uk.ac.rhul.cs2810;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import uk.ac.rhul.cs2810.util.DatabaseManager;

public class MenuItemManager {

  private static MenuItemManager instance = new MenuItemManager();
  private static String MENU_TABLE_NAME = "menu";

  private static DatabaseManager dm = DatabaseManager.getInstance();

  private MenuItemManager() {

  }

  /**
   * This creates and adds a new instance of a MenuItem to the database and returns the MenuItem
   * object instance that is newly created. This allows for the menu items to be updated on
   * different devices but still means that every gets the same menu items. As they can be added,
   * removed and edited remotely.
   * 
   * @param name The name of the item.
   * @param description A description of the item that appears with it.
   * @param calories The calorie count of the item.
   * @param category The category of the item.
   * @param type The type that the item is.
   * @param allergens The allergens, if any, that are present in the item.
   * @return
   */
  public MenuItem createNewMenuItem(String name, String description, int calories,
      Category category, ItemType type, EnumSet<Allergen> allergens, double cost) {
    // Check that the orders table exists, it if does not, then create it before inserting order
    if (!dm.tableExists(MENU_TABLE_NAME)) {
      createMenuTable();
    }

    int nextIdValue = dm.getNextAutoIncrement(MENU_TABLE_NAME);

    // Insert item into the database
    dm.execute(dm.createPreparedStatement("INSERT INTO `" + MENU_TABLE_NAME
        + "` (`id`, `name`, `description`, `calories`, `category`, `type`, `allergens`, `cost`) "
        + "VALUES (?,?,?,?,?,?,?,?)", nextIdValue, name, description, calories, category.name(),
        type.name(), serialiseAllergens(allergens), cost));

    System.out.println(
        "Menu Item [" + nextIdValue + "] has been created and inserted into the database!");
    return new MenuItem(nextIdValue, name, description, calories, category, type, allergens, cost);
  }

  /**
   * This can check the menu database for a specific item from its item identification number.
   * 
   * @param itemId This is the items unique identification number.
   * @return Whether the item was found in the datatebase table.
   */
  public boolean menuItemExists(int itemId) {
    ResultSet rs = dm.query(
        dm.createPreparedStatement("SELECT * FROM `" + MENU_TABLE_NAME + "` WHERE `id`=?", itemId));
    try {
      boolean exists = false;

      // If result row exists then the menu item exists.
      exists = rs.next();
      rs.getStatement().close();

      return exists;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * This can check the menu database for a specific item from its item name.
   * 
   * @param name This is the items name that appears in the menu table..
   * @return Whether the item was found in the database table.
   */
  public boolean menuItemExists(String name) {
    ResultSet rs = dm.query(
        dm.createPreparedStatement("SELECT * FROM `" + MENU_TABLE_NAME + "` WHERE `name`=?", name));
    try {
      boolean exists = false;

      exists = rs.next();
      rs.getStatement().close();

      return exists;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * This will delete and remove an menu item from the database menu item table identified by its
   * unique identification number.
   * 
   * @param itemId The identification number of the item, unique to itself.
   * @throws Exception Thrown when the order was not found in the table.
   */
  public void deleteMenuItem(int itemId) throws Exception {
    if (!menuItemExists(itemId)) {
      throw new Exception("The [Menu Item Id=" + itemId + "] does not exist!");
    }

    dm.execute(
        dm.createPreparedStatement("DELETE FROM `" + MENU_TABLE_NAME + "` WHERE id=?", itemId));
  }

  /**
   * This gets all the menu items that are present in the menu table in the database. Regardless of
   * any attributes.
   * 
   * @return List of menu items composed from the menu table.
   */
  public List<MenuItem> getAllMenuItems() {
    return getAllMenuItemsWhere("1");
  }

  /**
   * This will get a menu item object from the database identified by its unique identification
   * number.
   * 
   * @param itemId The identification number of the item.
   * @return The MenuItem object with the specified id number.
   */
  public MenuItem getMenuItemsByID(int itemId) {
    return getAllMenuItemsWhere("id='" + itemId + "'").get(0);
  }

  /**
   * This gets all the menu items from the menu table in the database, and sorts them so only the
   * items with matching allergen's are in the list.
   * 
   * @param allergen An allergen that the item may have.
   * @return List of menu items that all contain this allergen.
   */
  public List<MenuItem> getMenuItemsContainsAllergen(Allergen allergen) {
    return getAllMenuItemsWhere("allergens LIKE '%" + allergen.name() + "%'");
  }

  /**
   * This gets all the menu items from the menu table in the database, and sorts them so only the
   * items with matching categories are in the list.
   * 
   * @param category The category of item.
   * @return List of menu items that are all the same category.
   */
  public List<MenuItem> getMenuItemsByCategory(Category category) {
    return getAllMenuItemsWhere("category='" + category.name() + "'");
  }

  /**
   * This gets all the menu items from the menu table in the database, and sorts them so only the
   * items with matching types are in the list.
   * 
   * @param type The type of item.
   * @return List of menu items that are all the same type.
   */
  public List<MenuItem> getMenuItemsByType(ItemType type) {
    return getAllMenuItemsWhere("type='" + type.name() + "'");
  }

  /*
   * Gets all the items in the menu table where a condition applies. This method prevents
   * unnecessary duplication of code.
   */
  private List<MenuItem> getAllMenuItemsWhere(String where) {
    List<MenuItem> menuItems = new ArrayList<MenuItem>();

    ResultSet rs = dm.query(dm.createPreparedStatement(
        "SELECT * FROM `" + MENU_TABLE_NAME + "` WHERE " + where + " ORDER BY id ASC"));
    try {

      // Iterate through all of the rows in the result set
      while (rs.next()) {
        int itemId = rs.getInt("id");
        int calories = rs.getInt("calories");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Category category = Category.valueOf(rs.getString("category"));
        ItemType itemType = ItemType.valueOf(rs.getString("type"));
        EnumSet<Allergen> allergens = deserialiseAllergens(rs.getString("allergens"));
        double cost = rs.getDouble("cost");


        menuItems.add(
            new MenuItem(itemId, name, description, calories, category, itemType, allergens, cost));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return menuItems;
  }

  /*
   * Creates the item menu table with the specified attributes and primary key. This is used for the
   * first initialisation in a new system with no pre-made tables.
   */
  void createMenuTable() {
    if (dm.tableExists(MENU_TABLE_NAME)) {
      return;
    }
    // The table does not exist so it needs to be created.
    dm.execute(dm.createPreparedStatement("CREATE TABLE " + MENU_TABLE_NAME
        + "(id integer AUTO_INCREMENT," + "name varchar(64), description text, calories integer,"
        + "category VARCHAR(16), type varchar(16), allergens varchar(128), cost double, "
        + "PRIMARY KEY(id));"));

    System.out.println("Created " + MENU_TABLE_NAME + " table!");
  }

  /**
   * This will convert allergens in the menu item to a string that can be stored in the database,
   * and can be parsed back into a list of allergens from the string.
   * 
   * @param allergens The allergens to be serialised
   * @return The serialised string format of the allergens enum set
   */
  private String serialiseAllergens(EnumSet<Allergen> allergens) {
    if (allergens == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    Allergen[] a = new Allergen[allergens.size()];
    allergens.toArray(a);

    for (int i = 0; i < allergens.size(); i++) {
      if (i == allergens.size() - 1) { // Last element in array
        sb.append(a[i].name());
        continue;
      }
      sb.append(a[i].name() + ";");
    }
    return sb.toString();
  }

  /**
   * This will convert the serialised string format of the allergens enum set used by the database
   * back into a enum set of allergen enums.
   * 
   * @param allergenString The formated string of allergens.
   * @return A list of allergen enums.
   */
  private EnumSet<Allergen> deserialiseAllergens(String allergenString) {
    if (allergenString.isEmpty()) {
      return null;
    }
    String[] splitAllergenString = allergenString.split(";");

    List<Allergen> allergensList = new ArrayList<Allergen>();
    for (String a : splitAllergenString) {
      allergensList.add(Allergen.valueOf(a));
    }

    return EnumSet.copyOf(allergensList);

  }

  /** Returns the singleton instance of the MenuItemManager. */
  public static MenuItemManager getInstance() {
    return instance;
  }

}
