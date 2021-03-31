package cs3500.animator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Represents an abstract class for implementations of shapes.
 */
abstract class AbstractShape implements Shape {

  protected final String name;
  protected List<KeyMoment> keyMoments;

  /**
   * Constructs an AbstractShape, in terms of its name and its key moments in an animation.
   *
   * @param name       name of the shape
   * @param keyMoments its key moments in an animation
   */
  public AbstractShape(String name, List<KeyMoment> keyMoments) {
    this.name = name;
    this.keyMoments = keyMoments;
  }

  /**
   * Constructs an AbstractShape, in terms of its name with an empty list of key moments.
   *
   * @param name name of the shape
   */
  public AbstractShape(String name) {
    this.name = name;
    this.keyMoments = new ArrayList<KeyMoment>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public abstract Shape createCopyShape();

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof AbstractShape)) {
      return false;
    }

    AbstractShape that = (AbstractShape) a;

    return (this.name.equals(that.name)
            && this.keyMoments.containsAll(that.keyMoments)
            && that.keyMoments.containsAll(this.keyMoments));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.keyMoments);
  }

  @Override
  public void addAnimationShape(int startTick, Position2D startPosition, Dimensions startDimensions,
                                Color startColor, int endTick, Position2D endPosition,
                                Dimensions endDimensions, Color endColor) {
    ensureEndTickBiggerThanStart(startTick, endTick);
    if (ensureStartAndEndNotSame(startTick, startPosition, startDimensions, startColor, endTick,
            endPosition, endDimensions, endColor)) {
      if (!this.keyMoments.isEmpty()) {
        ensureStartElementAlignsToPrevious(startTick, startPosition, startDimensions, startColor);
        addNewStateOfShape(endTick, endPosition, endDimensions, endColor);
      } else {
        addStartingAnimation(startTick, startPosition, startDimensions, startColor, endTick,
                endPosition, endDimensions, endColor);
      }
    }
  }

  /**
   * Ensures that the end tick is bigger than the start tick so time is moving forward.
   *
   * @param startTick starting tick
   * @param endTick   ending tick
   * @throws IllegalArgumentException when the ending tick is smaller than the starting tick.
   */
  private void ensureEndTickBiggerThanStart(int startTick, int endTick) {
    if (endTick < startTick) {
      throw new IllegalArgumentException("End tick should be bigger than start tick");
    }
  }

  /**
   * Determines if the ending key moment is not the same as the starting key moment to avoid
   * repetition.
   *
   * @param startTick       starting tick
   * @param startPosition   starting 2D position
   * @param startDimensions starting dimensions
   * @param startColor      starting colour
   * @param endTick         ending tick
   * @param endPosition     ending 2D position
   * @param endDimensions   ending dimensions
   * @param endColor        ending colour
   * @return true if the starting and ending keymoments are not the same.
   */
  private boolean ensureStartAndEndNotSame(int startTick, Position2D startPosition,
                                           Dimensions startDimensions, Color startColor,
                                           int endTick, Position2D endPosition,
                                           Dimensions endDimensions, Color endColor) {
    KeyMoment initial = new KeyMoment(startTick, startColor, startDimensions, startPosition);
    KeyMoment end = new KeyMoment(endTick, endColor, endDimensions, endPosition);
    return !initial.equals(end);
  }

  /**
   * Adds both the starting and ending key moments which is also the starting animation (first
   * animation in the list) to this shape.
   *
   * @param startTick       starting tick
   * @param startPosition   starting 2D position
   * @param startDimensions starting dimensions
   * @param startColor      starting colour
   * @param endTick         ending tick
   * @param endPosition     ending 2D position
   * @param endDimensions   ending dimensions
   * @param endColor        ending colour
   */
  private void addStartingAnimation(int startTick, Position2D startPosition,
                                    Dimensions startDimensions, Color startColor, int endTick,
                                    Position2D endPosition, Dimensions endDimensions,
                                    Color endColor) {
    addNewStateOfShape(startTick, startPosition, startDimensions, startColor);
    addNewStateOfShape(endTick, endPosition, endDimensions, endColor);
  }

  /**
   * Ensures the given starting elements (dimension, position and tick) of the animation aligns with
   * the ending elements of the previous animation.
   *
   * @param startTick       starting tick
   * @param startPosition   starting position
   * @param startDimensions starting dimension
   * @param startColor      starting color
   * @throws IllegalArgumentException if the starting elements do not align with the ending elements
   *                                  of the previous animation.
   */
  private void ensureStartElementAlignsToPrevious(int startTick, Position2D startPosition,
                                                  Dimensions startDimensions, Color startColor) {

    KeyMoment currentStart = new KeyMoment(startTick, startColor, startDimensions, startPosition);
    if (!currentStart.equals(this.keyMoments.get(this.keyMoments.size() - 1))) {
      throw new IllegalArgumentException("Do not have common endpoint.");
    }
  }

  /**
   * Adds a new key moment to the shape with the given ending animation details.
   *
   * @param endTick       ending tick
   * @param endPosition   ending position
   * @param endDimensions ending dimension
   * @param endColor      ending colour
   */
  private void addNewStateOfShape(int endTick, Position2D endPosition,
                                  Dimensions endDimensions, Color endColor) {
    this.keyMoments.add(new KeyMoment(endTick, endColor, endDimensions, endPosition));
  }

  @Override
  public List<KeyMoment> getKeyMoments() {
    List<KeyMoment> newList = new ArrayList<KeyMoment>(this.keyMoments);
    return newList;
  }

  @Override
  public void removeKeyMomentAt(int tickOfAnimationToRemove) {
    this.keyMoments.remove(getThisKeyMoment(tickOfAnimationToRemove));
  }

  @Override
  public void addKeyframe(int tick, Position2D pos, Dimensions dim, Color color) {
    if (tickExists(tick)) {
      getThisKeyMoment(tick).updateKeyMoment(pos, dim, color);
    } else {
      addKeyMoment(tick, pos, dim, color);
    }
  }

  /**
   * Adds the key moment in the list in the right chronological order.
   *
   * @param tick  the tick
   * @param pos   the position
   * @param color the color
   * @param dim   the dimension
   * @throws IllegalArgumentException if either the pos, dim or color is null
   */
  private void addKeyMoment(int tick, Position2D pos, Dimensions dim, Color color) {
    if ((pos != null) && (dim != null) && (color != null)) {
      if (this.keyMoments.isEmpty()) {
        this.keyMoments.add(new KeyMoment(tick, color, dim, pos));
      } else {
        int firstTick = this.keyMoments.get(0).getTick();
        int lastTick = this.keyMoments.get(this.keyMoments.size() - 1).getTick();
        if (tick < firstTick) {
          this.keyMoments.add(0, new KeyMoment(tick, color, dim, pos));
        } else if (tick > lastTick) {
          this.keyMoments.add(new KeyMoment(tick, color, dim, pos));
        } else {
          for (int i = 0; i < this.keyMoments.size() - 1; i++) {
            int currentTick = this.keyMoments.get(i).getTick();
            int afterTick = this.keyMoments.get(i + 1).getTick();
            if ((tick < afterTick) && (tick > currentTick)) {
              this.keyMoments.add(i + 1, new KeyMoment(tick, color, dim, pos));
            }
          }
        }
      }
    } else {
      throw new IllegalArgumentException("Must have position, dimensions and color");
    }
  }

  /**
   * Ensures if this exact tick exists in the list of key moments.
   *
   * @param tick the tick to look for
   * @return true if the tick exists, false if not
   */
  private KeyMoment getThisKeyMoment(int tick) {
    for (KeyMoment km : this.keyMoments) {
      if (km.getTick() == tick) {
        return km;
      }
    }
    throw new IllegalArgumentException("No such keymoment.");
  }

  /**
   * Ensures if this exact tick exists in the list of key moments.
   *
   * @param tick the tick to look for
   * @return true if the tick exists, false if not
   */
  private boolean tickExists(int tick) {
    for (KeyMoment km : this.keyMoments) {
      return km.getTick() == tick;
    }
    return false;
  }

  /**
   * Creates a navigable map of all the key moments found in this shape with the tick as the key.
   *
   * @return a navigable map of all the key moments found in this shape with the tick as the key.
   */
  protected NavigableMap<Integer, KeyMoment> makeMap() {
    NavigableMap<Integer, KeyMoment> hMap = new TreeMap<>();
    for (KeyMoment k : this.keyMoments) {
      hMap.put(k.getTick(), k);
    }
    return hMap;
  }
}
