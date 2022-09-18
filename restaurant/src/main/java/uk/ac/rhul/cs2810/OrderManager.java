package uk.ac.rhul.cs2810;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.rhul.cs2810.util.DatabaseManager;

/**
 * The OrderManager is used to create Order objects, this manager also allows an API for
 * communicating with the database when receiving and inserting orders into the DB order table.
 * 
 * @author Marley Dey
 *
 */
public class OrderManager {

  private static OrderManager instance = new OrderManager();
  private static String ORDERS_TABLE_NAME = "orders";

  private DatabaseManager dm = DatabaseManager.getInstance();

  /*
   * Currently an empty constructor for providing the singleton design pattern.
   */
  private OrderManager() {
    // Empty for singleton design pattern reasons.
  }

  /**
   * This creates the Order object much like a Order factory, given the specific parameters, the
   * object is returned for local client usage, but also inserted into the database to be received
   * by other client systems.
   * 
   * @param menuItems The menu items that appear on the order.
   * @param tableNum The table number the order is associated to.
   * @return The Order object created by the factory.
   */
  public Order createOrder(Map<MenuItem, Integer> menuItems, int tableNum) {
    // Check that the orders table exists, it if does not, then create it before inserting order
    if (!dm.tableExists(ORDERS_TABLE_NAME)) {
      createOrderTable();
    }

    int nextIdValue = dm.getNextAutoIncrement(ORDERS_TABLE_NAME);

    long currentTime = System.currentTimeMillis();

    // Insert order into the database
    dm.execute(dm.createPreparedStatement("INSERT INTO `" + ORDERS_TABLE_NAME
        + "` (`id`, `table_num`, `time_placed`, `menu_items`, `status`) " + "VALUES (?,?,?,?,?)",
        nextIdValue, tableNum, currentTime, serialiseMenuItems(menuItems),
        OrderStatus.ORDERED.name()));

    System.out
        .println("Order [" + nextIdValue + "] has been created and inserted into the database!");
    return new Order(nextIdValue, menuItems, tableNum, currentTime, OrderStatus.ORDERED);
  }

  /**
   * This will delete an order from the database given its unique identifier.
   * 
   * @param orderId The identification number of the order to delete.
   */
  public void deleteOrder(int orderId) throws Exception {
    ResultSet rs = dm.query(
        dm.createPreparedStatement("SELECT * FROM " + ORDERS_TABLE_NAME + " WHERE id=?", orderId));
    try {
      if (!rs.next()) {
        rs.getStatement().close();
        throw new Exception("The [Order Id=" + orderId + "] does not exist!");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    dm.execute(
        dm.createPreparedStatement("DELETE FROM `" + ORDERS_TABLE_NAME + "` WHERE id=?", orderId));
  }

  /**
   * Gets a specific order in the database from its order identification number.
   * 
   * @param orderId The identification number of the order to get.
   * @return The order associated with the order identification number.
   */
  public Order getOrder(int orderId) {
    ResultSet rs = dm.query(dm.createPreparedStatement(
        "SELECT * FROM `" + ORDERS_TABLE_NAME + "` WHERE `id`=?", orderId));
    try {
      if (rs.next()) {
        int tableNum = rs.getInt("table_num");
        long timePlaced = rs.getLong("time_placed");
        String menuItemsText = rs.getString("menu_items");
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        return new Order(orderId, deserialiseMenuItems(menuItemsText), tableNum, timePlaced,
            status);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets all the orders in the orders database, of any status.
   * 
   * @return List of all orders in the database.
   */
  public List<Order> getAllOrders() {
    return getAllOrdersWhere("1");
  }

  /**
   * Gets all the orders in the database that are marked as the specified status.
   * 
   * @param status The status that the order must have to be returned in the list.
   * @return A list of all orders in the database with the status specified.
   */
  public List<Order> getAllOrdersByStatus(OrderStatus status) {
    return getAllOrdersWhere("status='" + status.name() + "'");

  }

  /**
   * Gets all the orders in the database that are marked as the specified status that also
   * corresponds to a specific table number.
   * 
   * @param status The status that the order must have to be returned in the list.
   * @param tableNum The table number all the orders are associated with.
   * @return The orders with the specified status for the table.
   */
  public List<Order> getAllOrdersByStatusAtTable(OrderStatus status, int tableNum) {
    return getAllOrdersWhere("status='" + status.name() + "' AND table_num=" + tableNum);
  }

  /*
   * This will create the order table in the database if it is not found.
   */
  void createOrderTable() {
    if (dm.tableExists(ORDERS_TABLE_NAME)) {
      return;
    }
    // The table does not exist so it needs to be created.
    dm.execute(dm.createPreparedStatement("CREATE TABLE " + ORDERS_TABLE_NAME
        + "(id integer AUTO_INCREMENT," + "table_num integer, time_placed bigint, menu_items text,"
        + "status VARCHAR(16), PRIMARY KEY(id));"));

    System.out.println("Created orders table!");
  }

  /**
   * This will convert menu item objects in the order to a string that can be stored in the
   * database, and can be parsed back into a map of menu items and their quantities from the string.
   * 
   * @param menuItems The items to be serialised
   * @return The serialised string format of the menu items list
   */
  private String serialiseMenuItems(Map<MenuItem, Integer> orderedItems) {
    // TODO Convert MenuItem objects to strings once they are complete
    StringBuilder sb = new StringBuilder();

    int i = 0;
    for (MenuItem item : orderedItems.keySet()) {   
      if (i == orderedItems.size() - 1) { // Last element
        sb.append(item.getMenuItemID() + "@" + orderedItems.get(item));
      } else {
        sb.append(item.getMenuItemID() + "@" + orderedItems.get(item) + ";");
      }
      
      i++;

    }

    return sb.toString();
  }

  /**
   * This will convert the serialised string format of the menu items used by the database back into
   * a list of menu item objects.
   * 
   * @param menuItems The formated string of menu items.
   * @return A list of menu item objects.
   */
  private Map<MenuItem, Integer> deserialiseMenuItems(String orderedItemsString) {
    
    MenuItemManager mim = MenuItemManager.getInstance();
    
    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    // TODO Convert string to MenuItem objects once they are complete
    String[] splitItemQuantityString = orderedItemsString.split(";");
    
    for (String itemQuantity : splitItemQuantityString) {
      String[] splitDetails = itemQuantity.split("@");
      
      MenuItem mi = mim.getMenuItemsByID(Integer.valueOf(splitDetails[0]));
      int quantity = Integer.valueOf(splitDetails[1]);
      
      items.put(mi,  quantity);
      
    }
     
    return items;
  }

  /*
   * Gets all the orders in the orders table where a condition applies. This method prevents
   * unnecessary duplication of code.
   */
  private List<Order> getAllOrdersWhere(String where) {
    List<Order> orders = new ArrayList<Order>();

    ResultSet rs = dm.query(dm.createPreparedStatement(
        "SELECT * FROM `" + ORDERS_TABLE_NAME + "` WHERE " + where + " ORDER BY id ASC"));
    try {

      // Iterate through all of the rows in the result set
      while (rs.next()) {
        int orderId = rs.getInt("id");
        int tableNum = rs.getInt("table_num");
        long timePlaced = rs.getLong("time_placed");
        String menuItemsText = rs.getString("menu_items");
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));


        orders.add(
            new Order(orderId, deserialiseMenuItems(menuItemsText), tableNum, timePlaced, status));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return orders;
  }

  /** Returns the singleton instance of the OrderManager. */
  public static OrderManager getInstance() {
    return instance;
  }

}
