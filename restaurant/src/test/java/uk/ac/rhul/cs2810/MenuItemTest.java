package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * This class uses JUnit test cases to test the methods of the MenuItem class.
 * 
 * @author Dora Rozsavolgyi
 * 
 */
public class MenuItemTest {

  private MenuItem menuItem;
  private EnumSet<Allergen> allergens;

  /**
   * Setting up tests by making an empty menu item and assigning example values to the allergens
   * EnumSet.
   */
  @BeforeEach
  public void setup() {
    // Making an enum set to be able to test the menu item constructor.
    allergens = EnumSet.of(Allergen.EGG, Allergen.MILK, Allergen.SOY);
  }

  // Creating a new item without allergens, checking if it does not retur null.
  @Test
  void testMenuItemConstructor1() {
    menuItem = new MenuItem(1, "pizza", "classic italian", 1200, Category.MAIN, ItemType.REGULAR,
        allergens, 12);

    assertNotNull(menuItem, "Constructing with valid values shouldn't return an empty item.");
  }

  // Creating a new item with allergens, checking if it does not return null.
  @Test
  void testMenuItemConstructor2() {
    menuItem = new MenuItem(1, "pizza", "classic italian", 1200, Category.MAIN, ItemType.REGULAR,
        null, 12);
    assertNotNull(menuItem, "Constructing with valid values shouldn't return an empty item.");
  }

  // Testing a getter method by using it on a newly created item, testing if it returns the correct
  // value.
  @Test
  void testGetter() {
    menuItem = new MenuItem(1, "pizza", "classic italian", 1200, Category.MAIN, ItemType.REGULAR,
        allergens, 12);
    assertEquals(menuItem.getCalories(), 1200,
        "Testing if the calorie getter method returns the correct value.");
  }

  // Changing the category of the item created to dessert to test the setter method, checking if it
  // worked with the getter method.
  @Test
  void testSetter() {
    menuItem = new MenuItem(1, "pizza", "classic italian", 1200, Category.MAIN, ItemType.REGULAR,
        allergens, 12);
    menuItem.setCategory(Category.DESSERT);
    assertEquals(menuItem.getCategory(), Category.DESSERT,
        "Testing if changing the category with the setter method changes it and returns the "
            + "correct new value.");
  }

}
