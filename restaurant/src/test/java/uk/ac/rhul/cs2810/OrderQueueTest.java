package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The class where all the tests of the Queue class are kept.
 * 
 * @author solsd
 *
 */
class OrderQueueTest {

  private static OrderQueue testingQueue;
  Order order1;
  Order order2;
  Order order3;
  Order order4;

  /**
   * Quick bit of code to initialise the queue before each of the tests.
   */
  @BeforeEach
  void test1_createQueue() {
    testingQueue = new OrderQueue();
    order1 = new Order(0, null, 3, 0, null);
    order2 = new Order(1, null, 5, 0, null);
    order3 = new Order(2, null, 2, 0, null);
    order4 = new Order(3, null, 10, 0, null);
  }

  /**
   * Test 2 checks if the addToQueue method does what it's been told to.
   */
  @Test
  void test2_addToQueue() {

    testingQueue.addToQueue(order1);

    assertEquals(1, testingQueue.checkSize(), "checkSize is wrong.");
  }

  /**
   * Test to see if the removal of an order from the queue works as expected.
   */
  @Test
  void test3_removeFromQueue() {
    testingQueue.addToQueue(order3);

    assertEquals(1, testingQueue.checkSize(), "addToQueue didn't add. I cri every tim.");

    assertEquals(order3, testingQueue.removeFromQueue(),
        "removeFromQueue has" + " decided to be an idiot.");
    assertEquals(0, testingQueue.checkSize(), "removeFromQueue isn't working.");
  }

  /**
   * This test sees if the method that finds out which order is next in the queue is behaving
   * properly.
   */
  @Test
  void test4_nextOrder() {
    testingQueue.addToQueue(order2);
    testingQueue.addToQueue(order1);
    testingQueue.addToQueue(order4);
    assertEquals(order1, testingQueue.getNextOrder(), "The next order is incorrect.");
  }

  /**
   * Test to see if nextOrders() method is working as expected.
   */
  @Test
  void test5_nextMultipleOrders() {
    testingQueue.addToQueue(order2);
    testingQueue.addToQueue(order1);
    testingQueue.addToQueue(order4);
    testingQueue.addToQueue(order3);
    testingQueue.addToQueue(order2);
    testingQueue.addToQueue(order3);

    assertTrue("0; 3; 2; ".equalsIgnoreCase(testingQueue.nextOrders(3)),
        "Test 5 isnt working reeeeee.");
  }

  /**
   * Makes sure the correct exception is thrown when trying to remove an item from an empty queue.
   */
  @Test
  void test6_removingEmptyQueue() {
    assertThrows(NoSuchElementException.class, () -> testingQueue.removeFromQueue());
  }

  /**
   * This one checks if the correct error is thrown when the range in nextOrders() is larger than
   * the size of the queue.
   */
  @Test
  void test7_tooManyNextOrders() {
    assertThrows(IndexOutOfBoundsException.class, () -> testingQueue.nextOrders(3));
    assertThrows(IndexOutOfBoundsException.class, () -> testingQueue.getNextOrder());
  }

  @Test
  void test8_findOrder() {
    testingQueue.addToQueue(order2);
    testingQueue.addToQueue(order1);
    testingQueue.addToQueue(order4);
    testingQueue.addToQueue(order3);
    assertEquals(1, testingQueue.findIndexOfOrder(0),
        "Finding the order failed. " + "We'll get 'em next time.");
  }

  /**
   * Test 9 ensures the correct exception is thrown.
   */
  @Test
  void test9_orderNotFound() {
    assertThrows(NoSuchElementException.class, () -> testingQueue.findIndexOfOrder(69));
  }
}
