package uk.ac.rhul.cs2810;

/**
 * The categories of MenuItem objects.
 * 
 * @author Dora Rozsavolgyi
 *
 */
public enum Category {
  STARTER("Starter"), MAIN("Main"), DESSERT("Dessert"), DRINK("Drink");

  private String category;

  /**
   * Assigns a String value to each category.
   * 
   * @param type is a String representation of a category.
   */
  Category(String category) {
    this.category = category;
  }

  /**
   * Returns a simple String representation of the category.
   */
  @Override
  public String toString() {
    return category;
  }
}
