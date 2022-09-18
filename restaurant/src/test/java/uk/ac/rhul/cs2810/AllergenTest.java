package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AllergenTest {

  private Allergen allergen;

  // Setting the allergen of a new item to wheat, testing if using the overridden toString method
  // returns the string representation of the allergen correctly.
  @Test
  void testToString() {
    allergen = Allergen.WHEAT;
    assertEquals(allergen.toString(), "Wheat",
        "Testing if the toString method returns the correct string for types.");
  }
}
