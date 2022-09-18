package uk.ac.rhul.cs2810;

/**
 * Status enumerator for the order to hold the different states it is in within the management
 * system. These states allow for a sequential process from one state to the next.
 * 
 * @author Marley Dey
 *
 */
public enum OrderStatus {
  ORDERED("Ordered"), PREPARED("Prepared"), DELIVERED("Delivered"), PAID("Paid");
  
  private String formatedName;
  
  private OrderStatus(String formatedName) {
    this.formatedName = formatedName;
  }
  
  public String toString() {
    return formatedName;
  }
  
}
