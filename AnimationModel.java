package cs3500.animator.model;

/**
 * This interface specifies the operations available to be used on an animation of 2D Shapes.
 */
public interface AnimationModel extends ReadOnlyAnimationModel {


  /**
   * Adds an animation to a given shape found in the model. The animation can change different
   * attributes of the shape simultaneously.
   *
   * @param startTick       starting tick
   * @param startPosition   starting 2D position
   * @param startDimensions starting dimensions
   * @param startColor      starting colour
   * @param endTick         ending tick
   * @param endPosition     ending 2D position
   * @param endDimensions   ending dimensions
   * @param endColor        ending colour
   * @throws IllegalArgumentException if any given tick is zero or negative or if the given shape
   *                                  cannot be found in the model or if the given starting
   *                                  specifications of the animation do not align with the ending
   *                                  specifications of the previous animation.
   */
  void addAnimation(String shapeToAnimate, int startTick, Position2D startPosition,
                    Dimensions startDimensions, Color startColor, int endTick,
                    Position2D endPosition, Dimensions endDimensions, Color endColor);

  /**
   * Adds a shape into the animation model.
   *
   * @param shapeToAdd shape to add
   * @throws IllegalArgumentException if the given shape is a duplicate of an existing shape, in
   *                                  terms of name.
   */
  void addShape(Shape shapeToAdd);

  /**
   * Sets the canvas dimensions into the model.
   *
   * @param left   left corner of the canvas
   * @param top    top corner of the canvas
   * @param width  width of the canvas
   * @param height height of the canvas
   * @throws IllegalArgumentException if the given shape is a duplicate of an existing shape, in
   *                                  terms of name.
   */
  void setCanvas(int left, int top, int width, int height);

  /**
   * Removes a shape into the animation model.
   *
   * @param shapeToRemove shape to remove
   * @throws IllegalArgumentException if the given shape is not found in the model.
   */
  void removeShape(String shapeToRemove);

  /**
   * Removes the key moment associated to the given shape and given tick.
   *
   * @param shapeToRemove           shape of the key moment to remove
   * @param tickOfAnimationToRemove tick associated to the key moment to remove
   * @throws IllegalArgumentException if the given shape is not found in the model or the tick
   *                                  cannot be found in the list of key moment that the shape
   *                                  holds.
   */
  void removeAnimation(String shapeToRemove, int tickOfAnimationToRemove);

  /**
   * Adds a keyframe to a given shape found in the model.
   *
   * @param shapeToAdd the shape to add the keyframe in
   * @param tick       the tick
   * @param pos        the position
   * @param dim        the dimensions
   * @param color      the color
   * @throws IllegalArgumentException if either the pos, dim or color is null
   */
  void addKeyframe(String shapeToAdd, int tick, Position2D pos,
                   Dimensions dim, Color color);

}
