package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ItemTypeTest {

  private ItemType itemType;

  // Setting the itemType of a new item to vegan, testing if using the overridden toString method
  // returns the string representation of the itemType correctly.
  @Test
  void testToString() {
    itemType = ItemType.VEGAN;
    assertEquals(itemType.toString(), "VG",
        "Testing if the toString method returns the correct string for types.");
  }
}
