package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.rhul.cs2810.util.DatabaseManager;

/**
 * Test case class for executing jUnit tests associated to the OrderManager Class.
 * 
 * @author Marley Dey
 *
 */
class OrderManagerTest {

  private static OrderManager om = null;
  private static MenuItemManager mim = null;

  @BeforeAll
  static void setup() {
    om = OrderManager.getInstance();
    mim = MenuItemManager.getInstance();
  }

  // Test that the order manager can only be assess as a single instance
  @Test
  void testSingletonInstance() {
    assertNotNull(om, "The instance of the OrderManager should not be NULL");
  }

  // Test created a order table if the table does not exist.
  @Test
  void testCreatesTableWhenMissing() {
    assertDoesNotThrow(() -> {
      om.createOrderTable();
    }, "Creating an order table should not throw any exceptions as they are handled");
  }

  // Test that you can create an order and insert it into the database
  @Test
  void testCreatingNewOrder() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order
    assertNotNull(om.createOrder(items, 3), "Creating an order should return an Order object");

    // Delete the order created
    om.deleteOrder(DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1);

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }

  // Test that you can delete an Order from the database
  @Test
  void testDeletingOrder() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order
    assertNotNull(om.createOrder(items, 6), "Creating an order should return an Order object");

    assertDoesNotThrow(() -> {
      om.deleteOrder(DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1);
    }, "Deleting an order that exists should not result in a exception being thrown");

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }

  // Test that you cannot delete an order that does not exist
  @Test
  void testDeletingNullOrder() {
    assertThrows(Exception.class, () -> {
      om.deleteOrder(0);
    }, "Trying to delete an Order that does not exist should throw an exeception");
  }

  // Test that you can create an order, receive it from the database, get the same details back then
  // delete it.
  @Test
  void testGettingAnExistingOrder() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order
    assertNotNull(om.createOrder(items, 7), "Creating an order should return an Order object");

    int id = DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1;

    // Get the created order
    assertDoesNotThrow(() -> {
      Order order = om.getOrder(id);
      assertEquals(7, order.getTableNum());
    }, "Getting an Order that exists should return the order instance and not throw "
        + "any exceptions");

    // Delete the order from the database
    assertDoesNotThrow(() -> {
      om.deleteOrder(id);
    }, "Deleting an order that exists should not cause any errors to be thrown");

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }

  // Test you can get all the order present in the database
  @Test
  void testGettingAllOrders() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order

    om.createOrder(items, 7);
    om.createOrder(items, 2);
    om.createOrder(items, 1);
    om.createOrder(items, 3);
    om.createOrder(items, 53);

    List<Order> orders = om.getAllOrders();

    assertNotNull(orders);

    for (Order o : orders) {
      System.out.println("ID found = " + o.getOrderId());
    }

    int id = DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1;

    // Delete orders that were created in the test
    for (int i = 0; i < 5; i++) {
      om.deleteOrder(id - i);
    }

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }

  // Test you can get all the prepared orders
  @Test
  void testGettingAllPreparedOrders() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("A Drink", "A nice refreshing drink", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);
    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 13 drinks.

    om.createOrder(items, 7);
    Order order1 = om.createOrder(items, 2);
    order1.updateStatus(OrderStatus.PREPARED);

    om.createOrder(items, 1);
    Order order2 = om.createOrder(items, 3);
    order2.updateStatus(OrderStatus.PREPARED);

    Order order3 = om.createOrder(items, 53);
    order3.updateStatus(OrderStatus.PREPARED);



    List<Order> orders = om.getAllOrdersByStatus(OrderStatus.PREPARED);

    assertNotNull(orders);

    for (Order o : orders) {
      System.out.println("ID found = " + o.getOrderId());
    }

    assertTrue(orders.size() >= 3, "There should be 3 (or more) prepared orders in the database.");

    int id = DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1;

    // Delete orders that were created in the test
    for (int i = 0; i < 5; i++) {
      om.deleteOrder(id - i);
    }

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }

  // Test you can get all the active orders for a specific table
  @Test
  void testGettingActiveOrdersToTable() throws Exception {
    MenuItem item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK,
        ItemType.VEGAN, null, 1);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order

    om.createOrder(items, 7);
    Order order1 = om.createOrder(items, 2);
    order1.updateStatus(OrderStatus.PREPARED);

    om.createOrder(items, 1);
    Order order2 = om.createOrder(items, 2);
    order2.updateStatus(OrderStatus.PREPARED);

    om.createOrder(items, 53);

    List<Order> orders = om.getAllOrdersByStatusAtTable(OrderStatus.PREPARED, 2);

    assertNotNull(orders);

    for (Order o : orders) {
      System.out.println("ID found = " + o.getOrderId());
    }

    assertTrue(orders.size() >= 2, "There should be 2 (or more) prepared orders in the database.");

    int id = DatabaseManager.getInstance().getNextAutoIncrement("orders") - 1;

    // Delete orders that were created in the test
    for (int i = 0; i < 5; i++) {
      om.deleteOrder(id - i);
    }

    // Delete the menu item
    mim.deleteMenuItem(DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1);
  }


}
