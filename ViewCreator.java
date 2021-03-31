package cs3500.animator.view;

import cs3500.animator.model.ReadOnlyAnimationModel;


/**
 * Represents a Factory class that creates different views depending on view type.
 */
public class ViewCreator {

  /**
   * Creates an instance of the view according to the given string which can either be "svg", "text"
   * or "visual".
   *
   * @return an AnimationView
   */
  public AnimationView create(String viewType, ReadOnlyAnimationModel m, String fileOutput,
                              int ticksPerSecond) {
    if (viewType.equalsIgnoreCase("svg")) {
      return new SVGView(m, fileOutput, ticksPerSecond);
    } else if (viewType.equalsIgnoreCase("text")) {
      return new TextualView(m, fileOutput);
    } else if (viewType.equalsIgnoreCase("visual")) {
      return new AnimationGraphicsView(m, ticksPerSecond);
    } else if (viewType.equalsIgnoreCase("edit")) {
      return new EditView(m, ticksPerSecond);
    } else {
      throw new IllegalArgumentException("View not eligible");
    }
  }

}
