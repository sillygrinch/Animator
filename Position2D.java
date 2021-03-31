package cs3500.animator.model;

import java.util.Objects;

/**
 * This class represents a 2D position used for animation.
 */

public class Position2D {

  private final int x;
  private final int y;

  /**
   * Initialize a position according to its x and y coordinates.
   *
   * @param x its x-coordinate
   * @param y its y-coordinate
   * @throws IllegalArgumentException if the coordinates are not positive.
   */
  public Position2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Copy constructor of a 2D position.
   *
   * @param v the position to copy
   */
  public Position2D(Position2D v) {
    this(v.x, v.y);
  }

  /**
   * Gets the x-coordinate of the position.
   *
   * @return the x-coordinate of the position.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y-coordinate of the position.
   *
   * @return the y-coordinate of the position.
   */
  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof Position2D)) {
      return false;
    }

    Position2D that = (Position2D) a;

    return ((this.x == that.x) && (this.y == that.y));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
}

