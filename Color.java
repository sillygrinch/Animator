package cs3500.animator.model;

import java.util.Objects;

/**
 * Represents a color, in terms of the red, green and blue colors that are added to produce this
 * specific color.
 */
public class Color {
  private final int r;
  private final int g;
  private final int b;

  /**
   * Constructs a color.
   *
   * @param r the r value in the RGB color model
   * @param g the g value in the RGB color model
   * @param b tthe b value in the RGB color model
   * @throws IllegalArgumentException if either the r, g or b values exceed the range 0 - 255.
   */
  public Color(int r, int g, int b) {
    if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) {
      throw new IllegalArgumentException("RGB must be in between 0 and 255");
    }
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Copy constructor of color.
   *
   * @param c the color to copy
   */
  public Color(Color c) {
    this(c.r, c.g, c.b);
  }

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof Color)) {
      return false;
    }

    Color that = (Color) a;

    return ((this.r == that.r) && (this.g == that.g) && (this.b == that.b));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.r, this.g, this.b);
  }

  /**
   * Getter method to get the R component of color.
   *
   * @return the R component of this color.
   */
  public int getR() {
    return r;
  }

  /**
   * Getter method to get the G component of color.
   *
   * @return the G component of this color.
   */
  public int getG() {
    return g;
  }

  /**
   * Getter method to get the B component of color.
   *
   * @return the B component of this color.
   */
  public int getB() {
    return b;
  }

}
