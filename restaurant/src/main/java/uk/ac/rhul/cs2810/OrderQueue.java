package uk.ac.rhul.cs2810;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * All the methods needed to manipulate the queue data structure used to store the Orders.
 * 
 * @author solsd
 *
 */
public class OrderQueue {

  private ArrayList<Order> queue; // This is the queue.

  /**
   * The default constructor that assigns the array list instance to the queue variable.
   */
  public OrderQueue() {
    queue = new ArrayList<Order>();
  }

  /**
   * Adds order to the back of the queue.
   * 
   * @param orderToBeAdded is the order we want to join the back of the queue
   */
  public void addToQueue(Order orderToBeAdded) {
    queue.add(orderToBeAdded);
  }

  /**
   * Takes the first element in the array and removes it.
   * 
   * @return the first Order in the queue
   */
  public Order removeFromQueue() {
    if (queue.size() == 0) {
      throw new NoSuchElementException();
    }
    Order orderToBeRemoved = queue.get(0);
    queue.remove(0);
    queue.trimToSize();
    return orderToBeRemoved;
  }

  /**
   * The method to find the length of the queue.
   * 
   * @return integer length of the queue
   */
  public int checkSize() {
    return queue.size();
  }

  /**
   * Method to get the second Order in the array.
   * 
   * @return second element in array
   */
  public Order getNextOrder() {
    if (queue.size() <= 1) {
      throw new IndexOutOfBoundsException();
    }
    return queue.get(1);
  }

  /**
   * Taking an integer parameter, this method finds the next n orders after the front of the queue.
   * 
   * @param range is the inclusive range of the next n orders
   * @return a string listing the IDs of each order in the range
   */
  public String nextOrders(int range) throws IndexOutOfBoundsException {
    if (queue.size() <= 1) {
      throw new IndexOutOfBoundsException();
    }
    String nextOrders = "";
    for (int i = 1; i <= range; i++) {
      Order order = queue.get(i);
      int addToString = order.getOrderId();
      nextOrders = nextOrders + addToString + "; ";
    }
    return nextOrders;
  }

  /**
   * This will return all the orders that are in the queue.
   * 
   * @return A list of all the orders in the queue.
   */
  public List<Order> getQueuedOrders() {
    if (queue.size() < 1) {
      return null;
    }

    return queue;
  }

  /**
   * This method is to find the index in which a specified order is located.
   *
   * @param id is the ID of the order that is to be found
   * @return the index of the queue where the Order is found
   */
  public int findIndexOfOrder(int id) {
    for (int i = 0; i < queue.size(); i++) {
      Order order = queue.get(i);
      if (order.getOrderId() == id) {
        return i;
      }
    }
    throw new NoSuchElementException();
  }

  /**
   * This method is to find the index in which a specified order is located.
   *
   * @param order The order to find in the queue.
   * @return the index of the queue where the Order is found
   */
  public int findIndexOfOrder(Order order) {
    for (int i = 0; i < queue.size(); i++) {
      Order queuedOrder = queue.get(i);
      if (order.getOrderId() == queuedOrder.getOrderId()) {
        return i;
      }
    }
    throw new NoSuchElementException();
  }
}
