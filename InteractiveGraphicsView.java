package cs3500.animator.view;

import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionListener;

/**
 * An interactive view for animations of shapes: display shapes on the panel and provide visual
 * interface for users and allow users to edit the animation.
 */
public interface InteractiveGraphicsView extends IVisualView {

  /**
   * Sets the action listener for the buttons to the given listener.
   *
   * @param listener the action listener.
   */
  void setActionListener(ActionListener listener);

  /**
   * Sets the list selection listener for the selection list to the given listener.
   *
   * @param listener the list selection listener.
   */
  void setListSelectionListener(ListSelectionListener listener);

  /**
   * Gets the user specified shape name of the shape the user would like to create.
   *
   * @return the name the user would like to name the shape.
   */
  String getShapeText();

  /**
   * Gets the user specified shape that the user would like to create.
   *
   * @return the type of shape the user would like to create.
   */
  String getShapeType();

  /**
   * Gets the user specified shape that the user would like to remove.
   *
   * @return the type of shape the user would like to remove.
   */
  String getShapeSelected();

  /**
   * Gets the user specified name of the shape that the user would like to edit (add or edit
   * existing keyframes).
   *
   * @return the name of the shape the user specified.
   */
  String getShapeNameText();

  /**
   * Gets the user specified position that the user would like to add or edit into a keyframe.
   *
   * @return the textual representation of the position.
   */
  String getShapePosXYText();

  /**
   * Gets the user specified color that the user would like to add or edit into a keyframe.
   *
   * @return the textual representation of the color.
   */
  String getShapeColRGBText();

  /**
   * Gets the user specified dimension that the user would like to add or edit into a keyframe.
   *
   * @return the textual representation of the dimension.
   */
  String getShapeDimText();

  /**
   * Gets the user specified tick of a keyframe that the user would like to add or edit.
   *
   * @return the textual representation of the tick.
   */
  String getShapeTickText();

  /**
   * Refreshes the panel with the list of keyframes of the chosen shape.
   */
  void refreshRemoveKeyFrameButtons();

  /**
   * Gets the name of the shape that the user would like to remove keyframes from.
   *
   * @return the name of the shape.
   */
  String getShapeToRemove();

  /**
   * Gets the tick of the keyframe the user would like to remove.
   *
   * @return the textual representation of the tick.
   */
  int getShapeToRemoveTick();

  /**
   * Updates the display to notify the user of the shape they created.
   *
   * @param name name of the shape created
   * @param type the type of the shape created
   */
  void updateCreateShapeDisplay(String name, String type);

  /**
   * Updates the display to notify the user of the shape they removed.
   *
   * @param removeThis name of the shape removed
   */
  void updateRemoveShapeDisplay(String removeThis);

  /**
   * Updates the display to notify the user of the keyframe they created.
   *
   * @param nameShape name of the shape the keyframe is created in
   * @param tickText  the tick
   * @param posn      the position
   * @param dim       the dimension
   * @param color     the color
   */
  void updateCreateKeyframeDisplay(String nameShape, String tickText, String posn,
                                   String dim, String color);

  /**
   * Updates the display to notify the user of the keyframe they removed.
   *
   * @param shapeName    name of the shape the keyframe is removed from
   * @param tickToRemove the tick of the keyframe that is removed
   */
  void updateRemoveKeyframeDisplay(String shapeName, int tickToRemove);

  /**
   * Saves the animation in an SVG format.
   */
  void saveSVGFile();

  int getTicksPerSec();

  void setPause();

  void setStartResume();

  void setRestart();
}
