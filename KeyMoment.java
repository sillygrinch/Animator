package cs3500.animator.model;

import java.util.Objects;

/**
 * Represents a KeyMoment which encapsulates the tick, colour, dimension and position a shape
 * experiences during an animation.
 */
public class KeyMoment {

  private final int tick;
  private Color color;
  private Dimensions dimensions;
  private Position2D position;

  /**
   * Constructs a KeyMoment of a 2D shape.
   *
   * @param tick       the tick of the key moment
   * @param color      the color of a shape
   * @param dimensions the dimensions of a shape
   * @param position   the 2D position of a shape
   * @throws IllegalArgumentException if any of the given elements are null values or if the tick is
   *                                  invalid, less than 1.
   */
  public KeyMoment(int tick, Color color, Dimensions dimensions, Position2D position) {
    if (color == null || dimensions == null || position == null) {
      throw new IllegalArgumentException("Elements cannot be null.");
    } else if (tick < 0) {
      throw new IllegalArgumentException("Tick must start at 0.");
    }
    this.tick = tick;
    this.color = color;
    this.dimensions = dimensions;
    this.position = position;
  }

  /**
   * Copy constructor of a key moment.
   *
   * @km the key moment to copy
   */
  public KeyMoment(KeyMoment km) {
    this(km.tick, km.color, km.dimensions, km.position);
  }

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof KeyMoment)) {
      return false;
    }

    KeyMoment that = (KeyMoment) a;

    return ((this.tick == that.tick)
            && (this.color.equals(that.color))
            && (this.position.equals(that.position))
            && this.dimensions.equals(that.dimensions));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tick, this.color, this.dimensions, this.position);
  }

  /**
   * Getter method to get the tick of this key moment.
   *
   * @return the tick of this key moment.
   */
  public int getTick() {
    return this.tick;
  }

  /**
   * Getter method to get the position of this key moment.
   *
   * @return a copy of the position of this key moment.
   */
  public Position2D getPos() {
    return new Position2D(this.position);
  }

  /**
   * Getter method to get the color of this key moment.
   *
   * @return a copy of the color of this key moment.
   */
  public Color getColor() {
    return new Color(this.color);
  }

  /**
   * Getter method to get the dimensions of this key moment.
   *
   * @return a copy of the dimensions of this key moment.
   */
  public Dimensions getDimensions() {
    return new Dimensions(this.dimensions);
  }

  /**
   * Updates the key moment to the given attributes, if not null.
   *
   * @param pos   the position
   * @param dim   the dimensions
   * @param color the color
   */
  public void updateKeyMoment(Position2D pos, Dimensions dim, Color color) {
    if (pos != null) {
      this.position = pos;
    }
    if (dim != null) {
      this.dimensions = dim;
    }
    if (color != null) {
      this.color = color;
    }
  }
}
