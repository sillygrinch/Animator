package cs3500.animator.model;

import java.util.List;

/**
 * This interface specifies all the operation that is available for a shape.
 */
public interface Shape {

  /**
   * Creates a copy of a 2D shape.
   *
   * @returns a copy of a 2D shape.
   */
  Shape createCopyShape();

  /**
   * Returns the name of the shape.
   *
   * @return a string representing the name of a shape.
   */
  String getName();

  /**
   * Adds an animation to the given shape.
   *
   * @param startTick       starting tick
   * @param startPosition   starting 2D position
   * @param startDimensions Starting dimensions
   * @param startColor      Starting colour
   * @param endTick         ending tick
   * @param endPosition     ending 2D position
   * @param endDimensions   ending dimensions
   * @param endColor        ending colour
   * @throws IllegalArgumentException if any given starting specification of the animation do not
   *                                  align with the ending specification of the previous
   *                                  animation.
   */
  void addAnimationShape(int startTick, Position2D startPosition, Dimensions startDimensions,
                         Color startColor, int endTick, Position2D endPosition,
                         Dimensions endDimensions, Color endColor);


  /**
   * Gets the list of key moments that the shape experiences.
   *
   * @return the list of key moments of the shape
   */
  List<KeyMoment> getKeyMoments();

  /**
   * Filters the shape to only include the key moments that are nearest to the given tick. If the
   * given tick is one of the tick of the key moments found in the shape, it will only return that
   * particular key moment associated to that tick. If the given tick is in between two key moments
   * of the shape, it will return both the closest key moments.
   *
   * @param tick tick
   * @return the shape with only the closest key moments to the given tick.
   */
  Shape getKeyMomentsAtTick(int tick);

  /**
   * Removes the key moment associated to that tick in the shape.
   *
   * @param tickOfAnimationToRemove tick of the key moment to remove.
   */
  void removeKeyMomentAt(int tickOfAnimationToRemove);

  /**
   * Adds a keyframe to this shape.
   *
   * @param tick  the tick
   * @param pos   the position
   * @param dim   the dimensions
   * @param color the color
   * @throws IllegalArgumentException if either the pos, dim or color is null
   */
  void addKeyframe(int tick, Position2D pos, Dimensions dim, Color color);
}
