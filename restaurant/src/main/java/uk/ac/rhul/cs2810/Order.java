package uk.ac.rhul.cs2810;

import java.text.SimpleDateFormat;
import java.util.Map;
import uk.ac.rhul.cs2810.util.DatabaseManager;

/**
 * The class where all the orders are created and the methods we use on them live.
 * 
 * @author solsd
 * @author Marley Dey
 *
 */
public class Order {

  private Map<MenuItem, Integer> orderedItems;
  private int orderId;
  private int tableNum;
  private long timePlaced;
  private OrderStatus status;

  /**
   * The only constructor, used by the OrderManager to parse details of an order from the database
   * to get back an order object.
   * 
   * @param orderId The identification number of the order given by the database primary key.
   * @param orderedItems The item objects from the menu that the order holds.
   * @param tableNum The table number the order is associated with.
   * @param timePlaced The time the order was placed.
   * @param status The status of the order, where it is in the process from ordered to paid.
   */
  public Order(int orderId, Map<MenuItem, Integer> orderedItems, int tableNum, long timePlaced,
      OrderStatus status) {
    // TODO String temporary, change to MenuItem when possible
    this.orderId = orderId;
    this.orderedItems = orderedItems;
    this.tableNum = tableNum;
    // timePlaced is a large number as it is milliseconds since 1/1/1970
    this.timePlaced = timePlaced;
    this.status = status;

  }

  /**
   * This gets all the menu items associated with this order. (aka. All the menu items the customer
   * has ordered.)
   * 
   * @return List of menu items the customer has ordered.
   */
  public Map<MenuItem, Integer> getOrderedItems() {
    return orderedItems;
  }

  /**
   * The method can be used to find the table number associated with the order, amongst other things
   * I don't have time to go into.
   * 
   * @return the table number the order came from
   */
  public int getTableNum() {
    return tableNum;
  }

  /**
   * This method is designed to find out how long it has been since the order was created.
   * 
   * @return the time since the order was made
   */
  public String getTimeSinceOrder() {
    long currentTime = System.currentTimeMillis();

    // This will find how long the order has existed.
    long timeSinceOrder = currentTime - timePlaced;

    // Formats the time difference in milliseconds to a String of minutes.
    SimpleDateFormat time = new SimpleDateFormat("mm");
    return time.format(timeSinceOrder);
  }

  /** Returns the time the order was placed. */
  public long getTimePlaced() {
    return timePlaced;
  }

  /** Returns the status of the order. */
  public OrderStatus getOrderStatus() {
    return status;
  }

  /** Return whether the order has been ordered. */
  public boolean isOdered() {
    return status == OrderStatus.ORDERED;
  }

  /**
   * Sometimes, it's important to know if an order has been prepared. This method checks for us so
   * we don't have to. Isn't that brilliant?
   * 
   * @return whether the order is prepared or not
   */
  public boolean isPrepared() {
    return status == OrderStatus.PREPARED;
  }

  /** Return whether the order has been delivered. */
  public boolean isDelivered() {
    return status == OrderStatus.DELIVERED;
  }

  /** Return whether the order has been paid. */
  public boolean isPaid() {
    return status == OrderStatus.PAID;
  }

  /**
   * This allows you to update the status of the order. This also updates the database value.
   */
  public void updateStatus(OrderStatus status) {
    DatabaseManager dm = DatabaseManager.getInstance();
    this.status = status;

    dm.execute(dm.createPreparedStatement("UPDATE `orders` SET status=? WHERE id=?", status.name(),
        orderId));

  }

  /**
   * Calculates the total cost of the order given all the ordered items and their quantities.
   * 
   * @return The orders total cost.
   */
  public double getTotalBill() {
    double total = 0.0;

    Map<MenuItem, Integer> items = getOrderedItems();
    if (items == null) {
      return 0.0;
    }
    for (MenuItem mi : items.keySet()) {
      int quantity = items.get(mi);
      total = total + (mi.getCost() * quantity);
    }

    return total;
  }

  /** Returns the order identification number of the order. */
  public int getOrderId() {
    return orderId;
  }
}
