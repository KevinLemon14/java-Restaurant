package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The testing class for the Order class.
 * 
 * @author solsd
 *
 */
class OrdersTest {

  private static Order order1; // Creating a new Order called order1, here's one I prepared earlier.
  private static MenuItemManager mim = null;
  private static MenuItem item1;

  @BeforeAll
  static void setup() {
    mim = MenuItemManager.getInstance();
    item1 = mim.createNewMenuItem("test1", "test description", 1, Category.DRINK, ItemType.VEGAN,
        null, 1);
  }

  @AfterAll
  static void setdown() throws Exception {
    mim.deleteMenuItem(item1.getMenuItemID());
  }

  /**
   * This just adds a little bit of spice to order1 before each of the tests so they can run
   * properly.
   */
  @BeforeEach
  void test1_creatingOrder() {

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 13); // 12 test1 items in the order

    // Orders are made up of a list of dishes and the table number that the order came from.
    order1 = new Order(0, items, 3, 0, OrderStatus.ORDERED);
  }

  /**
   * This test checks what dishes/recipes make up the order. (Basically tells the kitchen what to
   * make.)
   */
  @Test
  void test2_getMenuItems() {
    assertNotNull(order1.getOrderedItems(), "Test 2 does not function correctly");
  }

  /**
   * For this test, we ask the order to tell us which table it came from. If you ask really nicely
   * it might even tell you how to get there.
   */
  @Test
  void test3_getTableNum() {
    assertEquals(3, order1.getTableNum(), "Check getTableNum(), it's not working.");
  }

  /**
   * Test to check if an order has been completed by kitchen staff.
   */
  @Test
  void test5_isPrepared() {
    assertFalse(order1.isPrepared(), "isCompleted needs some work, and so do you frankly.");
  }

  /**
   * This test tests to see if tests completeOrder tests works like it's tests meant to tests.
   * Sorry, I have tourtests.
   */
  @Test
  void test6_completeOrder() {
    order1.updateStatus(OrderStatus.PREPARED);;
    assertTrue(order1.isPrepared(), "I have some good news and bad news. "
        + "Good news is that completeOrder isn't working properly");
  }

  // Test that you can get the calculated total bill cost.
  @Test
  void test7_getTotalBill() throws Exception {
    MenuItem item1 =
        mim.createNewMenuItem("test", "test descr", 100, Category.DRINK, ItemType.VEGAN, null, 12);
    MenuItem item2 = mim.createNewMenuItem("test 2", "test descr", 100, Category.DRINK,
        ItemType.VEGAN, null, 12);

    Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    items.put(item1, 2);
    items.put(item2, 2);

    OrderManager om = OrderManager.getInstance();
    
    Order order = om.createOrder(items, 3);
    
    

    assertNotNull(order.getOrderedItems(), "Items in the order should not be null");
    assertEquals(48, order.getTotalBill(), "The bill total should be 48");
    
    om.deleteOrder(order.getOrderId());
    
    mim.deleteMenuItem(item1.getMenuItemID());
    mim.deleteMenuItem(item2.getMenuItemID());
    
    
  }
}
