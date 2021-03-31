package cs3500.animator.model;

import java.util.Objects;

/**
 * Represents Dimensions in terms of width and height of a 2D shape.
 */
public class Dimensions {

  private final int width;
  private final int height;

  /**
   * Constructs a Dimensions in terms of width and height.
   *
   * @param height the height
   * @param width  the width
   * @throws IllegalArgumentException if the width or height is zero or negative.
   */
  public Dimensions(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive.");
    }
    this.width = width;
    this.height = height;
  }

  /**
   * Copy constructor of a Dimensions.
   *
   * @param d the Dimensions to copy
   */
  public Dimensions(Dimensions d) {
    this(d.width, d.height);
  }

  /**
   * Gets the width of a Dimensions.
   *
   * @return the width of a Dimensions.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets the height of a Dimensions.
   *
   * @return the height of a Dimensions.
   */
  public int getHeight() {
    return this.height;
  }


  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof Dimensions)) {
      return false;
    }

    Dimensions that = (Dimensions) a;

    return ((this.height == that.height) && (this.width == that.width));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.width, this.height);
  }
}

