package uk.ac.rhul.cs2810;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.rhul.cs2810.util.DatabaseManager;

class MenuItemManagerTest {

  private static MenuItemManager mim = null;

  @BeforeAll
  static void setup() {
    mim = MenuItemManager.getInstance();
  }

  // Test that the menu item manager can only be assess as a single instance
  @Test
  void testSingletonInstance() {
    assertNotNull(mim, "The instance of the MenuItemManager should not be NULL");
  }

  // Test that the menu table is created if it doesnt exist
  @Test
  void testMenuTableCreation() {
    assertDoesNotThrow(() -> {
      mim.createMenuTable();
    }, "This should create the menu table without throwing any exceptions");
  }

  // Test that you can create a new MenuItem and it is inserted into the database correctly.
  @Test
  void testAddingNewMenuItem() throws Exception {
    assertDoesNotThrow(() -> {
      assertNotNull(mim.createNewMenuItem("Marley's Fish & Chips", "Amazing, fresh batter.", 12,
          Category.MAIN, ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG, Allergen.MILK),
          1));
      assertNotNull(mim.createNewMenuItem("Marley's Fish & Chips 2", "Amazing, fresh batter.", 12,
          Category.MAIN, ItemType.REGULAR, EnumSet.of(Allergen.FISH), 1));
      assertNotNull(mim.createNewMenuItem("Marley's Fish & Chips 3", "Amazing, fresh batter.", 12,
          Category.MAIN, ItemType.REGULAR, null, 1));
    }, "Adding a new menu item to the database should not throw any exceptions");

    int id = DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1;

    // Restore original state of table
    for (int i = id; i > id - 3; i--) {
      mim.deleteMenuItem(i);
    }
  }

  // Test is an item is in the menu table.
  @Test
  void testItemAppearsInTable() throws Exception {
    mim.createNewMenuItem("Marley's Fish & Chips", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG, Allergen.MILK), 1);

    int id = DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1;
    assertTrue(mim.menuItemExists(id), "The newly created item should exist in the database.");

    // Restore original state of table
    mim.deleteMenuItem(id);
  }

  // Test that you can delete an item from the items table.
  @Test
  void testDeletingItems() throws Exception {
    mim.createNewMenuItem("Marley's Fish & Chips", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG, Allergen.MILK), 1);

    int id = DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1;

    assertDoesNotThrow(() -> {
      mim.deleteMenuItem(id);

    });

    assertFalse(mim.menuItemExists(id), "The newly deleted item should exist in the database.");

  }

  // Test getting item that contains a specific allergen
  @Test
  void testGettingSpecificAllergen() throws Exception {
    mim.createNewMenuItem("Marley's Fish & Chips", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG, Allergen.MILK), 1);
    mim.createNewMenuItem("Marley's Fish & Chips 2", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.MILK), 1);
    mim.createNewMenuItem("Marley's Fish & Chips 3", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG), 1);

    List<MenuItem> items = null;

    assertNotNull(items = mim.getMenuItemsContainsAllergen(Allergen.MILK));

    for (MenuItem mi : items) {
      System.out
          .println("ID of items with milk ID = " + mi.getMenuItemID() + " Name = " + mi.getName());
    }

    int id = DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1;

    // Restore original state of table
    for (int i = id; i > id - 3; i--) {
      mim.deleteMenuItem(i);
    }
  }

  // Test getting item that contains a specific type
  @Test
  void testGettingSpecificType() throws Exception {
    mim.createNewMenuItem("Marley's Fish & Chips", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.REGULAR, EnumSet.of(Allergen.FISH, Allergen.EGG, Allergen.MILK), 1);
    mim.createNewMenuItem("Marley's Fish & Chips 2", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.VEGAN, EnumSet.of(Allergen.FISH, Allergen.MILK), 1);
    mim.createNewMenuItem("Marley's Fish & Chips 3", "Amazing, fresh batter.", 12, Category.MAIN,
        ItemType.VEGETARIAN, EnumSet.of(Allergen.FISH, Allergen.EGG), 1);

    List<MenuItem> items = null;

    assertNotNull(items = mim.getMenuItemsByType(ItemType.VEGAN));

    for (MenuItem mi : items) {
      System.out.println(
          "ID of item that is VEGAN ID = " + mi.getMenuItemID() + " Name = " + mi.getName());
    }

    int id = DatabaseManager.getInstance().getNextAutoIncrement("menu") - 1;

    // Restore original state of table
    for (int i = id; i > id - 3; i--) {
      mim.deleteMenuItem(i);
    }

  }


}
