package uk.ac.rhul.cs2810;

/**
 * The types of MenuItem objects.
 * 
 * @author Dora Rozsavolgyi
 *
 */
public enum ItemType {
  REGULAR(""), VEGETARIAN("V"), VEGAN("VG");

  private String type;

  /**
   * Assigns a String value to each type.
   * 
   * @param type is a String representation of a type.
   */
  ItemType(String type) {
    this.type = type;
  }

  /**
   * Returns a simple String representation of a type.
   */
  @Override
  public String toString() {
    return type;
  }
}
