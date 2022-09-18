package uk.ac.rhul.cs2810;

import java.util.EnumSet;

public class MenuItem {

  private int itemId;
  private int calories;
  private double cost;
  private String name;
  private String description;
  private Category category;
  private ItemType itemType;
  private EnumSet<Allergen> allergens;

  /**
   * Constructor for a MenuItem object. The allergens need to be passed in as an EnumSet (because
   * one object can only be assigned one enum value regularly), this can be done by doing e.g.
   * allergens = EnumSet.of(Allergen.EGG, Allergen.MILK)
   * 
   * @param name is the name of the item
   * @param description is a brief description of the item
   * @param calories is integer value of the calorie count of the item
   * @param category is whether an item is a starter,main,dessert or a drink
   * @param type is whether an item is vegetarian, vegan or regular
   * @param allergens is an EnumSet of Allergen of the allergens contained in the item, read method
   *        description to understand how to implement
   */
  public MenuItem(int itemId, String name, String description, int calories, Category category,
      ItemType type, EnumSet<Allergen> allergens, double cost) {
    this.itemId = itemId;
    this.name = name;
    this.description = description;
    this.calories = calories;
    this.category = category;
    this.itemType = type;
    this.allergens = allergens;
    this.cost = cost;
  }

  /**
   * Getter method for item names.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter method for item descriptions.
   * 
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Getter method for item claorie count.
   * 
   * @return the calories
   */
  public int getCalories() {
    return calories;
  }

  /** Returns the cost of the item. */
  public double getCost() {
    return cost;
  }

  /**
   * Getter method for item category.
   * 
   * @return the category
   */
  public Category getCategory() {
    return category;
  }

  /**
   * Getter method for item types.
   * 
   * @return the itemType
   */
  public ItemType getType() {
    return itemType;
  }

  /**
   * Getter method for item allergens.
   * 
   * @return the allergens
   */
  public EnumSet<Allergen> getAllergens() {
    return allergens;
  }

  /** Returns the items Identification number. */
  public int getMenuItemID() {
    return itemId;
  }

  /**
   * Setter method for item names.
   * 
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Setter method for item descriptions.
   * 
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Setter method for item calorie count.
   * 
   * @param calories the calories to set
   */
  public void setCalories(int calories) {
    this.calories = calories;
  }

  /**
   * Setter method for item category.
   * 
   * @param category the category to set
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Setter method for item type.
   * 
   * @param itemType the itemType to set
   */
  public void setType(ItemType itemType) {
    this.itemType = itemType;
  }

  /**
   * Setter method for item allergens.
   * 
   * @param allergens the allergens to set
   */
  public void setAllergens(EnumSet<Allergen> allergens) {
    this.allergens = allergens;
  }

}
