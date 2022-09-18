package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CategoryTest {

  private Category category;

  // Setting the category of a new item to dessert, testing if using the overridden toString method
  // returns the string representation of the category correctly.
  @Test
  void testToString() {
    category = Category.DESSERT;
    assertEquals(category.toString(), "Dessert",
        "Testing if the toString method returns the correct string for types.");
  }
}
