package uk.ac.rhul.cs2810;

/**
 * The allergen types of MenuItem objects.
 * 
 * @author Dora Rozsavolgyi
 *
 */
public enum Allergen {
  MILK("Milk"), EGG("Egg"), NUTS("Nuts"), FISH("Fish"), WHEAT("Wheat"), SOY("Soy");


  private String allergen;

  /**
   * Assigns a String value to each allergen.
   * 
   * @param allergen is a String representation of an allergen.
   */
  Allergen(String allergen) {
    this.allergen = allergen;
  }

  /**
   * Returns a simple String representation of an allergen.
   */
  @Override
  public String toString() {
    return allergen;
  }
}
