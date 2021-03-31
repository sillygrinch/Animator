package cs3500.animator.model;

import java.util.List;

/**
 * Read only model, to retrieve information of the model.
 */
public interface ReadOnlyAnimationModel {

  /**
   * Gets the list of shapes found in the model.
   *
   * @return a list of shapes found in the model.
   */
  List<Shape> getShapesInModel();

  /**
   * Gets the dimensions of the canvas used for this animation.
   *
   * @return an array of int representing the canvas left, top, width and height, in this order.
   */
  int[] getCanvasDimensions();

  /**
   * Gets the list of shapes (with only the closest key moments to that tick) that should be
   * participating in the animation at the given tick. The return list should have the same size as
   * the list of shapes found in the model whereby null representing that the shape is not found at
   * the given tick.
   *
   * @return a list of shapes at the given tick.
   */
  List<Shape> getShapesAtTick(int t);
}
